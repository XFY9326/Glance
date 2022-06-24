package io.github.xfy9326.glance.ui.screen.analysis

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.github.xfy9326.glance.io.FileManager
import io.github.xfy9326.glance.ml.MLManager
import io.github.xfy9326.glance.ml.beans.DetectResult
import io.github.xfy9326.glance.ml.beans.ModelType
import io.github.xfy9326.glance.tools.suspendLazy
import io.github.xfy9326.glance.ui.data.AnalysisItem
import io.github.xfy9326.glance.ui.data.AnalysisResult
import io.github.xfy9326.glance.ui.data.AnalyzingImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AnalysisViewModel constructor(private val imageUri: Uri) : ViewModel() {
    val analyzingImage = AnalyzingImage(imageUri)
    private val cachedAnalysisResult by suspendLazy { analyzeImage() }
    private val _analysisResult = MutableStateFlow<AnalysisResult>(AnalysisResult.Processing)
    val analysisResult = _analysisResult.asStateFlow()

    init {
        initImageAnalysing()
    }

    private fun initImageAnalysing() {
        viewModelScope.launch {
            _analysisResult.value = cachedAnalysisResult.value()
        }
    }

    private suspend fun analyzeImage(): AnalysisResult {
        return FileManager.readBitmap(imageUri).fold(
            onSuccess = {
                val labels = MLManager.loadLabels(ModelType.GENERAL_MODEL)
                val model = MLManager.getModel(ModelType.GENERAL_MODEL)
                when (val result = model.detectByBitmap(it, MLManager.hasGPUSupport())) {
                    is DetectResult.ModelInitFailed -> AnalysisResult.ModelLoadFailed
                    is DetectResult.Success -> AnalysisResult.Success(
                        result.objects.map { obj ->
                            AnalysisItem.from(labels, obj)
                        }.sortedByDescending { item ->
                            item.reliability
                        }
                    )
                }
            },
            onFailure = {
                AnalysisResult.ImageLoadFailed
            }
        )
    }
}

class AnalysisViewModelFactory(private val imageUri: Uri) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == AnalysisViewModel::class.java) { "Wrong viewModel class $modelClass" }
        return modelClass.getConstructor(Uri::class.java).newInstance(imageUri)
    }
}

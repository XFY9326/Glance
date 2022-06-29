package io.github.xfy9326.glance.ui.screen.analysis

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.github.xfy9326.atools.coroutines.suspendLazy
import io.github.xfy9326.atools.io.okio.readBitmapAsync
import io.github.xfy9326.glance.ml.MLManager
import io.github.xfy9326.glance.ui.data.AnalysisResult
import io.github.xfy9326.glance.ui.data.AnalyzingImage
import io.github.xfy9326.glance.ui.data.convertToImageObjectInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AnalysisViewModel constructor(private val imageUri: Uri) : ViewModel() {
    private val confThreshold = 0.25f
    private val iouThreshold = 0.45f

    val analyzingImage = AnalyzingImage(imageUri)
    private val cachedAnalysisResult by suspendLazy { analyzeImage() }
    private val _analysisResult = MutableStateFlow<AnalysisResult>(AnalysisResult.Initializing)
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
        return imageUri.readBitmapAsync().fold(
            onSuccess = {
                val model = MLManager.getDetectionModel()
                val labels = model.loadLabels().getOrNull() ?: return@fold AnalysisResult.LabelsLoadFailed
                val result = model.detectByBitmap(it, confThreshold, iouThreshold).getOrNull() ?: return@fold AnalysisResult.ModelLoadFailed
                AnalysisResult.Success(result.convertToImageObjectInfo(labels))
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

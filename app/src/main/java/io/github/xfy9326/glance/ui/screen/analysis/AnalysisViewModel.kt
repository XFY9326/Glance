package io.github.xfy9326.glance.ui.screen.analysis

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.github.xfy9326.glance.io.FileManager
import io.github.xfy9326.glance.ml.MLManager
import io.github.xfy9326.glance.ml.beans.ModelType
import io.github.xfy9326.glance.tools.suspendLazy
import io.github.xfy9326.glance.ui.base.AnalyzingImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AnalysisViewModel constructor(private val imageUri: Uri) : ViewModel() {
    val analyzingImage = AnalyzingImage(imageUri)

    private val detectModel by lazy { MLManager.getModel(ModelType.GENERAL_MODEL) }
    private val detectLabels by suspendLazy(Dispatchers.IO) { MLManager.loadLabels(ModelType.GENERAL_MODEL) }
    private val detectResult by suspendLazy {
        val imageBitmap = FileManager.readBitmap(imageUri)
        detectModel.detectByBitmap(imageBitmap.getOrThrow(), MLManager.hasGPUSupport())
    }

    private val _analyzeText = MutableStateFlow("")
    val analyzeText = _analyzeText.asStateFlow()

    init {
        analyzeImage()
    }

    private fun analyzeImage() {
        viewModelScope.launch {
            val labels = detectLabels.value()
            val result = detectResult.value()?.map { labels[it.classId] }
            _analyzeText.tryEmit(result.toString())
        }
    }
}

class AnalysisViewModelFactory(private val imageUri: Uri) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == AnalysisViewModel::class.java) { "Wrong viewModel class $modelClass" }
        return modelClass.getConstructor(Uri::class.java).newInstance(imageUri)
    }
}

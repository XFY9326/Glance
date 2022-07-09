package io.github.xfy9326.glance.ui.base

import androidx.annotation.CallSuper
import androidx.camera.core.ImageAnalysis
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.xfy9326.atools.coroutines.suspendLazy
import io.github.xfy9326.glance.ml.MLManager
import io.github.xfy9326.glance.ml.beans.ImageInfo
import io.github.xfy9326.glance.ui.data.AnalysisResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors

abstract class CameraAnalysisViewModel(private val requestCaption: Boolean) : ViewModel() {
    private val imageAnalysisExecutor = Executors.newSingleThreadExecutor()
    private val confThreshold = 0.5f
    private val iouThreshold = 0.45f

    private val _classLabels by suspendLazy { MLManager.loadClasses() }
    private val _captionVocabulary by suspendLazy { MLManager.loadVocabulary() }

    private val _analysisResult = MutableStateFlow<AnalysisResult>(AnalysisResult.Initializing)
    val analysisResult = _analysisResult.asStateFlow()

    init {
        // Preload
        viewModelScope.launch {
            _classLabels.value()
            if (requestCaption) {
                _captionVocabulary.value()
            }
        }
    }

    fun setImageAnalysisAnalyzer(imageAnalysis: ImageAnalysis) {
        imageAnalysis.setAnalyzer(imageAnalysisExecutor) {
            it.use {
                runBlocking {
                    val result = MLManager.analyzeImageByPixelsData(it.toPixelsData(), confThreshold, iouThreshold, requestCaption).getOrNull()
                    if (result == null) {
                        _analysisResult.value = AnalysisResult.ModelProcessFailed
                    } else {
                        _analysisResult.value = onProcessImageInfo(result)
                    }
                }
            }
        }
    }

    suspend fun getClassLabels() = _classLabels.value().getOrNull()

    suspend fun getCaptionVocabulary() = _captionVocabulary.value().getOrNull()

    protected abstract suspend fun onProcessImageInfo(imageInfo: ImageInfo): AnalysisResult

    @CallSuper
    override fun onCleared() {
        try {
            if (!imageAnalysisExecutor.isShutdown) {
                imageAnalysisExecutor.shutdown()
            }
        } catch (e: Exception) {
            // Ignore
        }
    }
}
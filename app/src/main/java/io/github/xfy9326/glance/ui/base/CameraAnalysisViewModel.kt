package io.github.xfy9326.glance.ui.base

import androidx.annotation.CallSuper
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import io.github.xfy9326.atools.coroutines.suspendLazy
import io.github.xfy9326.glance.ml.MLManager
import io.github.xfy9326.glance.ui.data.AnalysisResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors

abstract class CameraAnalysisViewModel : ViewModel() {
    private val imageAnalysisExecutor = Executors.newSingleThreadExecutor()

    private val _classLabels by suspendLazy { MLManager.loadClasses() }
    private val _captionVocabulary by suspendLazy { MLManager.loadVocabulary() }

    private val _analysisResult = MutableStateFlow<AnalysisResult>(AnalysisResult.Initializing)
    val analysisResult = _analysisResult.asStateFlow()

    fun setImageAnalysisAnalyzer(imageAnalysis: ImageAnalysis) {
        imageAnalysis.setAnalyzer(imageAnalysisExecutor) {
            it.use {
                runBlocking {
                    _analysisResult.value = onAnalyzeImage(it)
                }
            }
        }
    }

    suspend fun getClassLabels() = _classLabels.value().getOrNull()

    suspend fun getCaptionVocabulary() = _captionVocabulary.value().getOrNull()

    protected abstract suspend fun onAnalyzeImage(imageProxy: ImageProxy): AnalysisResult

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
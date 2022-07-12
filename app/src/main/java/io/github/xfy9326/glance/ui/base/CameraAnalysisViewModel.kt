package io.github.xfy9326.glance.ui.base

import androidx.annotation.CallSuper
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import io.github.xfy9326.atools.coroutines.suspendLazyValue
import io.github.xfy9326.glance.ml.MLManager
import io.github.xfy9326.glance.ui.data.AnalysisResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors
import kotlin.time.Duration

abstract class CameraAnalysisViewModel : ViewModel() {
    private val imageAnalysisExecutor = Executors.newSingleThreadExecutor()
    private var lastAnalysisOutputMills = 0L

    private val _classLabels = suspendLazyValue { MLManager.loadClasses() }
    private val _captionVocabulary = suspendLazyValue { MLManager.loadVocabulary() }

    private val _analysisResult = MutableStateFlow<AnalysisResult>(AnalysisResult.Initializing)
    val analysisResult = _analysisResult.asStateFlow()

    fun setImageAnalysisAnalyzer(imageAnalysis: ImageAnalysis) {
        imageAnalysis.setAnalyzer(imageAnalysisExecutor) {
            it.use {
                runBlocking {
                    val minIntervalMills = getAnalysisMinInterval().inWholeMilliseconds
                    if (minIntervalMills == 0L || System.currentTimeMillis() - lastAnalysisOutputMills > minIntervalMills) {
                        _analysisResult.value = onAnalyzeImage(it)
                        lastAnalysisOutputMills = System.currentTimeMillis()
                    }
                }
            }
        }
    }

    suspend fun getClassLabels() = _classLabels.value().getOrNull()

    suspend fun getCaptionVocabulary() = _captionVocabulary.value().getOrNull()

    protected abstract suspend fun onAnalyzeImage(imageProxy: ImageProxy): AnalysisResult

    protected open suspend fun getAnalysisMinInterval(): Duration = Duration.ZERO

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
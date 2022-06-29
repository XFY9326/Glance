package io.github.xfy9326.glance.ui.screen.finder

import androidx.camera.core.ImageAnalysis
import androidx.lifecycle.ViewModel
import io.github.xfy9326.atools.coroutines.suspendLazy
import io.github.xfy9326.glance.ml.MLManager
import io.github.xfy9326.glance.ui.base.toPixelsData
import io.github.xfy9326.glance.ui.data.AnalysisResult
import io.github.xfy9326.glance.ui.data.convertToImageObjectInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors

class FinderViewModel : ViewModel() {
    private val imageAnalysisExecutor = Executors.newSingleThreadExecutor()
    private val confThreshold = 0.6f
    private val iouThreshold = 0.45f
    private val resultTakeAmount = 2

    private val detectionModel = MLManager.getDetectionModel()
    private val detectionLabels by suspendLazy { detectionModel.loadLabels() }

    private val _analysisResult = MutableStateFlow<AnalysisResult>(AnalysisResult.Initializing)
    val analysisResult = _analysisResult.asStateFlow()

    fun setImageAnalysisAnalyzer(imageAnalysis: ImageAnalysis) {
        imageAnalysis.setAnalyzer(imageAnalysisExecutor) {
            it.use {
                runBlocking {
                    val labels = detectionLabels.value().getOrNull()
                    if (labels == null) {
                        _analysisResult.value = AnalysisResult.LabelsLoadFailed
                    } else {
                        val result = detectionModel.detectByPixelsData(it.toPixelsData(), confThreshold, iouThreshold).getOrNull()
                        if (result == null) {
                            _analysisResult.value = AnalysisResult.ModelLoadFailed
                        } else {
                            _analysisResult.value = AnalysisResult.Success(
                                result.convertToImageObjectInfo(labels, resultTakeAmount)
                            )
                        }
                    }
                }
            }
        }
    }

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

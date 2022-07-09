package io.github.xfy9326.glance.ui.screen.finder

import androidx.camera.core.ImageAnalysis
import androidx.lifecycle.ViewModel
import io.github.xfy9326.atools.coroutines.suspendLazy
import io.github.xfy9326.glance.ml.MLManager
import io.github.xfy9326.glance.ui.base.toPixelsData
import io.github.xfy9326.glance.ui.data.AnalysisResult
import io.github.xfy9326.glance.ui.data.toImageObjectInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors

class FinderViewModel : ViewModel() {
    private val imageAnalysisExecutor = Executors.newSingleThreadExecutor()
    private val confThreshold = 0.5f
    private val iouThreshold = 0.45f
    private val resultTakeAmount = 3

    private val classLabels by suspendLazy { MLManager.loadClasses() }

    private val _analysisResult = MutableStateFlow<AnalysisResult>(AnalysisResult.Initializing)
    val analysisResult = _analysisResult.asStateFlow()

    fun setImageAnalysisAnalyzer(imageAnalysis: ImageAnalysis) {
        imageAnalysis.setAnalyzer(imageAnalysisExecutor) {
            it.use {
                runBlocking {
                    val labels = classLabels.value().getOrNull()
                    if (labels == null) {
                        _analysisResult.value = AnalysisResult.LabelsLoadFailed
                    } else {
                        val result = MLManager.analyzeImageByPixelsData(it.toPixelsData(), confThreshold, iouThreshold, false).getOrNull()
                        if (result == null) {
                            _analysisResult.value = AnalysisResult.ModelProcessFailed
                        } else {
                            val imageObjectInfo = result.toImageObjectInfo(labels) {
                                take(
                                    resultTakeAmount
                                ).sortedBy { obj ->
                                    obj.classId
                                }
                            }
                            _analysisResult.value = AnalysisResult.Success(imageObjectInfo)
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

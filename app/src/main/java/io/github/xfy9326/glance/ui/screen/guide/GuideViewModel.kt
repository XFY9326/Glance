package io.github.xfy9326.glance.ui.screen.guide

import androidx.camera.core.ImageAnalysis
import androidx.lifecycle.ViewModel
import io.github.xfy9326.atools.coroutines.suspendLazy
import io.github.xfy9326.glance.ml.MLManager
import io.github.xfy9326.glance.ml.beans.ModelType
import io.github.xfy9326.glance.ui.base.toPixelsData
import io.github.xfy9326.glance.ui.data.AnalysisResult
import io.github.xfy9326.glance.ui.data.convertToAnalysisResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors

class GuideViewModel : ViewModel() {
    private val imageAnalysisExecutor = Executors.newSingleThreadExecutor()

    private val classLabels by suspendLazy { MLManager.loadLabels(ModelType.GUIDE_MODEL) }
    private val mlModel = MLManager.getModel(ModelType.GUIDE_MODEL)

    private val _analysisResult = MutableStateFlow<AnalysisResult>(AnalysisResult.Initializing)
    val analysisResult = _analysisResult.asStateFlow()

    fun setImageAnalysisAnalyzer(imageAnalysis: ImageAnalysis) {
        imageAnalysis.setAnalyzer(imageAnalysisExecutor) {
            it.use {
                runBlocking {
                    val result = mlModel.detectByPixelsData(it.toPixelsData())
                    _analysisResult.value = result.convertToAnalysisResult(classLabels.value())
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

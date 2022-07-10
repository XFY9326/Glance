package io.github.xfy9326.glance.ui.screen.scene

import androidx.camera.core.ImageProxy
import io.github.xfy9326.glance.ml.MLManager
import io.github.xfy9326.glance.ui.base.CameraAnalysisViewModel
import io.github.xfy9326.glance.ui.base.toPixelsData
import io.github.xfy9326.glance.ui.data.AnalysisResult
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class SceneViewModel : CameraAnalysisViewModel() {

    override suspend fun getAnalysisMinInterval(): Duration {
        return 2.seconds
    }

    override suspend fun onAnalyzeImage(imageProxy: ImageProxy): AnalysisResult {
        val vocabulary = getCaptionVocabulary() ?: return AnalysisResult.ResourcesLoadFailed
        val result = MLManager.analyzeImageCaptionByPixelsData(imageProxy.toPixelsData())
            .getOrNull() ?: return AnalysisResult.ModelProcessFailed
        val imageCaption = MLManager.parseCaptionIds(result, vocabulary)
        return AnalysisResult.CaptionGenerateSuccess(imageCaption)
    }
}
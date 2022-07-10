package io.github.xfy9326.glance.ui.screen.guide

import androidx.camera.core.ImageProxy
import io.github.xfy9326.glance.ml.MLManager
import io.github.xfy9326.glance.ml.beans.getCaptionText
import io.github.xfy9326.glance.ui.base.CameraAnalysisViewModel
import io.github.xfy9326.glance.ui.base.toPixelsData
import io.github.xfy9326.glance.ui.data.AnalysisResult
import io.github.xfy9326.glance.ui.data.toImageObjectInfo

class GuideViewModel : CameraAnalysisViewModel() {
    companion object {
        private const val confThreshold = 0.5f
        private const val iouThreshold = 0.45f
    }

    override suspend fun onAnalyzeImage(imageProxy: ImageProxy): AnalysisResult {
        val labels = getClassLabels() ?: return AnalysisResult.ResourcesLoadFailed
        val vocabulary = getCaptionVocabulary() ?: return AnalysisResult.ResourcesLoadFailed
        val result = MLManager.analyzeImageByPixelsData(
            data = imageProxy.toPixelsData(),
            confThreshold = confThreshold,
            iouThreshold = iouThreshold,
            requestCaption = true
        ).getOrNull() ?: return AnalysisResult.ModelProcessFailed
        val imageObjectInfo = result.toImageObjectInfo(labels) {
            this
        }
        val imageCaption = result.getCaptionText(vocabulary)
        return AnalysisResult.Success(imageObjectInfo, imageCaption)
    }
}
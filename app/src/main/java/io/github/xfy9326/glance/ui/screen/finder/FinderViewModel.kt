package io.github.xfy9326.glance.ui.screen.finder

import androidx.camera.core.ImageProxy
import io.github.xfy9326.glance.ml.MLManager
import io.github.xfy9326.glance.ui.base.CameraAnalysisViewModel
import io.github.xfy9326.glance.ui.base.toPixelsData
import io.github.xfy9326.glance.ui.data.AnalysisResult
import io.github.xfy9326.glance.ui.data.toImageObjectInfo

class FinderViewModel : CameraAnalysisViewModel() {
    companion object {
        private const val resultTakeAmount = 3
        private const val confThreshold = 0.5f
        private const val iouThreshold = 0.45f
    }

    override suspend fun onAnalyzeImage(imageProxy: ImageProxy): AnalysisResult {
        val labels = getClassLabels() ?: return AnalysisResult.ResourcesLoadFailed
        val result = MLManager.analyzeImageByPixelsData(
            data = imageProxy.toPixelsData(),
            confThreshold = confThreshold,
            iouThreshold = iouThreshold,
            requestCaption = false
        ).getOrNull() ?: return AnalysisResult.ModelProcessFailed
        val imageObjectInfo = result.toImageObjectInfo(labels) {
            take(resultTakeAmount).sortedBy { obj ->
                obj.classId
            }
        }
        return AnalysisResult.Success(imageObjectInfo)
    }
}

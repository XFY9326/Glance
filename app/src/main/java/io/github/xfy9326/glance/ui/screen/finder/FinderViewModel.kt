package io.github.xfy9326.glance.ui.screen.finder

import io.github.xfy9326.glance.ml.beans.ImageInfo
import io.github.xfy9326.glance.ui.base.CameraAnalysisViewModel
import io.github.xfy9326.glance.ui.data.AnalysisResult
import io.github.xfy9326.glance.ui.data.toImageObjectInfo

class FinderViewModel : CameraAnalysisViewModel(false) {
    companion object {
        private const val resultTakeAmount = 3
    }

    override suspend fun onProcessImageInfo(imageInfo: ImageInfo): AnalysisResult {
        val labels = getClassLabels() ?: return AnalysisResult.ResourcesLoadFailed
        val imageObjectInfo = imageInfo.toImageObjectInfo(labels) {
            take(
                resultTakeAmount
            ).sortedBy { obj ->
                obj.classId
            }
        }
        return AnalysisResult.Success(imageObjectInfo)
    }
}

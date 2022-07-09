package io.github.xfy9326.glance.ui.screen.guide

import io.github.xfy9326.glance.ml.MLManager
import io.github.xfy9326.glance.ml.beans.ImageInfo
import io.github.xfy9326.glance.ui.base.CameraAnalysisViewModel
import io.github.xfy9326.glance.ui.data.AnalysisResult
import io.github.xfy9326.glance.ui.data.toImageObjectInfo

class GuideViewModel : CameraAnalysisViewModel(true) {

    override suspend fun onProcessImageInfo(imageInfo: ImageInfo): AnalysisResult {
        val labels = getClassLabels() ?: return AnalysisResult.ResourcesLoadFailed
        val vocabulary = getCaptionVocabulary() ?: return AnalysisResult.ResourcesLoadFailed
        val imageObjectInfo = imageInfo.toImageObjectInfo(labels)
        val imageCaption = imageInfo.captionIds?.let { ids -> MLManager.parseCaptionIds(ids, vocabulary) }
        return AnalysisResult.Success(imageObjectInfo, imageCaption)
    }
}
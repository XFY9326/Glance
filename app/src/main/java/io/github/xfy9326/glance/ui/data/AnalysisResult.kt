package io.github.xfy9326.glance.ui.data

import io.github.xfy9326.glance.ml.beans.DetectResult

sealed interface AnalysisResult {
    object Initializing : AnalysisResult
    object ImageLoadFailed : AnalysisResult
    object ModelLoadFailed : AnalysisResult
    class Success(val imageObjectInfo: ImageObjectInfo) : AnalysisResult
}

fun DetectResult.convertToAnalysisResult(labels: Array<String>): AnalysisResult =
    when (this) {
        DetectResult.ModelInitFailed -> AnalysisResult.ModelLoadFailed
        is DetectResult.Success -> AnalysisResult.Success(detectInfo.convertToImageObjectInfo(labels))
    }

fun AnalysisResult.Success.hasObjects(): Boolean =
    imageObjectInfo.objects.isNotEmpty()

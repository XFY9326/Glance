package io.github.xfy9326.glance.ui.data

sealed interface AnalysisResult {
    object Initializing : AnalysisResult
    object ImageLoadFailed : AnalysisResult
    object ModelLoadFailed : AnalysisResult
    object LabelsLoadFailed : AnalysisResult
    class Success(val imageObjectInfo: ImageObjectInfo) : AnalysisResult
}

fun AnalysisResult.Success.hasObjects(): Boolean =
    imageObjectInfo.objects.isNotEmpty()

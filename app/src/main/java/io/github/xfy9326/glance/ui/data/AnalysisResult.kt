package io.github.xfy9326.glance.ui.data

sealed interface AnalysisResult {
    object Initializing : AnalysisResult
    object ImageLoadFailed : AnalysisResult
    object ModelProcessFailed : AnalysisResult
    object ResourcesLoadFailed : AnalysisResult

    class DetectSuccess(
        val imageObjectInfo: ImageObjectInfo,
        val caption: String? = null
    ) : AnalysisResult

    class CaptionGenerateSuccess(
        val caption: String
    ) : AnalysisResult
}

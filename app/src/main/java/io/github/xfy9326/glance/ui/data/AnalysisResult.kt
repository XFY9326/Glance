package io.github.xfy9326.glance.ui.data

sealed interface AnalysisResult {
    object Processing : AnalysisResult
    object ImageLoadFailed : AnalysisResult
    object ModelLoadFailed : AnalysisResult
    class Success(val analysisItem: List<AnalysisItem>) : AnalysisResult
}
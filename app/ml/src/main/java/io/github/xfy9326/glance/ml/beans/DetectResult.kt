package io.github.xfy9326.glance.ml.beans

sealed interface DetectResult {
    object ModelInitFailed : DetectResult
    class Success(val detectInfo: DetectInfo) : DetectResult
}
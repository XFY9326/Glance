package io.github.xfy9326.glance.ml.beans

sealed interface DetectResult {
    object ModelInitFailed : DetectResult
    class Success(
        val imageWidth: Int,
        val imageHeight: Int,
        val objects: Array<DetectObject>
    ) : DetectResult
}
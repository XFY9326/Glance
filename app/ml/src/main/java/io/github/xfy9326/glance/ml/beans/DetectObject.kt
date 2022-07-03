package io.github.xfy9326.glance.ml.beans

import androidx.annotation.Keep

@Keep
data class DetectObject internal constructor(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float,
    val confidence: Float,
    val classId: Int,
) {
    val width: Float
        get() = right - left

    val height: Float
        get() = bottom - top
}
package io.github.xfy9326.glance.ml.beans

import android.graphics.RectF
import androidx.annotation.Keep

@Keep
data class DetectObject(
    val box: RectF,
    val classId: Int,
    val confidence: Float
) {
    constructor(left: Float, top: Float, right: Float, bottom: Float, classId: Int, confidence: Float) :
            this(RectF(left, top, right, bottom), classId, confidence)
}
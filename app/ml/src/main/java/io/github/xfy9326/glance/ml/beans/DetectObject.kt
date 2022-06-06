package io.github.xfy9326.glance.ml.beans

import android.graphics.RectF

data class DetectObject(
    val box: RectF,
    val class_id: Int,
    val confidence: Float
) {
    constructor(left: Float, top: Float, right: Float, bottom: Float, class_id: Int, confidence: Float) :
            this(RectF(left, top, right, bottom), class_id, confidence)
}
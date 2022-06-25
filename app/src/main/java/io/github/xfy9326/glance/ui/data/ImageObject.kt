package io.github.xfy9326.glance.ui.data

import android.graphics.RectF
import io.github.xfy9326.glance.ml.beans.DetectObject
import kotlin.math.roundToInt

data class ImageObject(
    val classText: String,
    val reliability: Int,
    val box: RectF
) {
    companion object {
        fun from(labels: Array<String>, detectObject: DetectObject) =
            ImageObject(
                classText = labels[detectObject.classId],
                reliability = (detectObject.confidence * 100).roundToInt(),
                box = RectF(detectObject.box)
            )
    }
}
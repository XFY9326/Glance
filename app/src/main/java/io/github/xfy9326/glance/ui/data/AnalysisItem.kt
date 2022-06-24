package io.github.xfy9326.glance.ui.data

import android.graphics.RectF
import io.github.xfy9326.glance.ml.beans.DetectObject

data class AnalysisItem(
    val classText: String,
    val reliability: Float,
    val locationBox: RectF
) {
    companion object {
        fun from(labels: Array<String>, detectObject: DetectObject) =
            AnalysisItem(
                classText = labels[detectObject.classId],
                reliability = detectObject.confidence,
                locationBox = RectF(detectObject.box)
            )
    }
}
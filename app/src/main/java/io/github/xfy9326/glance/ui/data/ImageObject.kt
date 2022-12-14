package io.github.xfy9326.glance.ui.data

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import io.github.xfy9326.glance.ml.beans.DetectObject
import io.github.xfy9326.glance.ml.beans.TextLabels
import kotlin.math.roundToInt

data class ImageObject(
    val classId: Int,
    val classText: String,
    val reliability: Int,
    val offset: Offset,
    val size: Size
)

fun DetectObject.toImageObject(labels: TextLabels) =
    ImageObject(
        classId = classId,
        classText = labels[classId],
        reliability = (confidence * 100).roundToInt(),
        offset = Offset(left, top),
        size = Size(width, height)
    )

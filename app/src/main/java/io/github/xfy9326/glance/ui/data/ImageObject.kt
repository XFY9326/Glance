package io.github.xfy9326.glance.ui.data

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import io.github.xfy9326.glance.ml.beans.DetectObject
import kotlin.math.roundToInt

data class ImageObject(
    val classText: String,
    val reliability: Int,
    val offset: Offset,
    val size: Size
)

fun DetectObject.convertToImageObject(labels: Array<String>) =
    ImageObject(
        classText = labels[classId],
        reliability = (confidence * 100).roundToInt(),
        offset = Offset(box.left, box.top),
        size = Size(box.width(), box.height())
    )

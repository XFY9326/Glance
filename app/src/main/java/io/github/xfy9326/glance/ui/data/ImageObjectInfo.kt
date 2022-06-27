package io.github.xfy9326.glance.ui.data

import androidx.compose.ui.geometry.Size
import io.github.xfy9326.glance.ml.beans.DetectInfo

data class ImageObjectInfo(
    val size: Size,
    val objects: List<ImageObject>
)

fun DetectInfo.convertToImageObjectInfo(labels: Array<String>): ImageObjectInfo =
    ImageObjectInfo(
        size = Size(width.toFloat(), height.toFloat()),
        objects = objects.asSequence().map {
            it.convertToImageObject(labels)
        }.sortedByDescending {
            it.reliability
        }.toList()
    )

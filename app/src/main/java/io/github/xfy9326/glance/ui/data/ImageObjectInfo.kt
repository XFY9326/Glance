package io.github.xfy9326.glance.ui.data

import androidx.compose.ui.geometry.Size
import io.github.xfy9326.glance.ml.beans.ImageInfo

data class ImageObjectInfo(
    val size: Size,
    val objects: List<ImageObject>
)

fun ImageInfo.toImageObjectInfo(
    labels: Array<String>,
    onMap: Sequence<ImageObject>.() -> Sequence<ImageObject>
): ImageObjectInfo =
    ImageObjectInfo(
        size = Size(width.toFloat(), height.toFloat()),
        objects = objects.asSequence().map {
            it.toImageObject(labels)
        }.run(onMap).toList()
    )

fun List<ImageObject>.countOutputClasses(): List<Pair<String, Int>> {
    val combinedMap = LinkedHashMap<String, Int>()
    forEach {
        combinedMap[it.classText] = combinedMap.getOrDefault(it.classText, 0) + 1
    }
    return combinedMap.toList()
}
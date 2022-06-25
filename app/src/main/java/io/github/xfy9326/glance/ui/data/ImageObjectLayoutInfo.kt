package io.github.xfy9326.glance.ui.data

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import kotlin.math.min

data class ImageObjectLayoutInfo(
    val imageObject: ImageObject,
    val boxOffset: Offset,
    val boxSize: Size
)

fun ImageObjectInfo.calculateLayout(layoutSize: Size): List<ImageObjectLayoutInfo> {
    val ratio = min(layoutSize.width / size.width, layoutSize.height / size.height)
    val scaledWidth = min(size.width * ratio, layoutSize.width)
    val scaledHeight = min(size.height * ratio, layoutSize.height)
    val scaledOffset = Offset((layoutSize.width - scaledWidth) / 2f, (layoutSize.height - scaledHeight) / 2f)
    return objects.map {
        ImageObjectLayoutInfo(
            imageObject = it,
            boxOffset = it.offset * ratio + scaledOffset,
            boxSize = it.size * ratio
        )
    }
}

package io.github.xfy9326.glance.ui.data

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import kotlin.math.min
import kotlin.math.roundToInt

data class ImageObjectLayoutInfo(
    val imageObject: ImageObject,
    val boxOffset: IntOffset,
    val boxSize: IntSize
)

fun ImageObjectInfo.fitLayoutSize(layoutWidth: Int, layoutHeight: Int): List<ImageObjectLayoutInfo> {
    val ratio = min(layoutWidth / size.width, layoutHeight / size.height)
    val scaledWidth = min(size.width * ratio, layoutWidth.toFloat())
    val scaledHeight = min(size.height * ratio, layoutHeight.toFloat())
    val scaledOffsetX = ((layoutWidth - scaledWidth) / 2f)
    val scaledOffsetY = ((layoutHeight - scaledHeight) / 2f)

    return objects.map {
        val x = (it.offset.x * ratio + scaledOffsetX).roundToInt()
        val y = (it.offset.y * ratio + scaledOffsetY).roundToInt()
        val w = (it.size.width * ratio).roundToInt()
        val h = (it.size.height * ratio).roundToInt()
        ImageObjectLayoutInfo(
            imageObject = it,
            boxOffset = IntOffset(x, y),
            boxSize = IntSize(w, h)
        )
    }
}

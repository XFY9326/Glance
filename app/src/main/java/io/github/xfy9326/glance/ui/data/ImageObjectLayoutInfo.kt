package io.github.xfy9326.glance.ui.data

import android.graphics.RectF
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.toAndroidRectF
import kotlin.math.abs
import kotlin.math.min

data class ImageObjectLayoutInfo(
    val imageObject: ImageObject,
    val boxOffset: Offset,
    val boxSize: Size
)

fun ImageObjectLayoutInfo.getAndroidRectF(): RectF =
    Rect(boxOffset, boxSize).toAndroidRectF()

fun ImageObjectInfo.calculateLayout(width: Float, height: Float): List<ImageObjectLayoutInfo> {
    if (abs(size.width - width) < 1e-3 && abs(size.height - height) < 1e-3) {
        return objects.map {
            ImageObjectLayoutInfo(
                imageObject = it,
                boxOffset = it.offset,
                boxSize = it.size
            )
        }
    } else {
        val ratio = min(width / size.width, height / size.height)
        val scaledWidth = min(size.width * ratio, width)
        val scaledHeight = min(size.height * ratio, height)
        val scaledOffset = Offset((width - scaledWidth) / 2f, (height - scaledHeight) / 2f)
        return objects.map {
            ImageObjectLayoutInfo(
                imageObject = it,
                boxOffset = it.offset * ratio + scaledOffset,
                boxSize = it.size * ratio
            )
        }
    }
}

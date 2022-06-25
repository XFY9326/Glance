package io.github.xfy9326.glance.ui.common

import android.graphics.RectF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.xfy9326.glance.ui.data.ImageObject
import io.github.xfy9326.glance.ui.theme.AppTheme
import kotlin.math.min

@Preview(showBackground = true)
@Composable
private fun PreviewImageObjectBoxLayer() {
    AppTheme {
        ImageObjectBoxLayer(
            imageSize = Size(500f, 500f),
            imageObjects = listOf(
                ImageObject("Class 1", 100, RectF(50f, 50f, 100f, 100f)),
                ImageObject("Class 2", 90, RectF(100f, 100f, 150f, 150f)),
                ImageObject("Class 3", 80, RectF(200f, 200f, 250f, 250f)),
                ImageObject("Class 4", 70, RectF(350f, 350f, 400f, 400f))
            ),
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
fun ImageObjectBoxLayer(
    imageSize: Size,
    imageObjects: List<ImageObject>,
    modifier: Modifier,
    boxStroke: Dp = 2.dp,
    boxStrokeColor: Color = Color.Red
) {
    Canvas(
        modifier = modifier,
        onDraw = {
            val ratio = min(size.width / imageSize.width, size.height / imageSize.height)
            val scaledWidth = min((imageSize.width * ratio).toInt(), size.width.toInt())
            val scaledHeight = min((imageSize.height * ratio).toInt(), size.height.toInt())
            val leftOffset = (size.width - scaledWidth) / 2
            val topOffset = (size.height - scaledHeight) / 2
            imageObjects.forEach {
                val l = it.box.left * ratio + leftOffset
                val t = it.box.top * ratio + topOffset
                val w = it.box.width() * ratio
                val h = it.box.height() * ratio
                drawRoundRect(boxStrokeColor, topLeft = Offset(l, t), size = Size(w, h), style = Stroke(width = boxStroke.toPx()))
            }
        }
    )
}
package io.github.xfy9326.glance.ui.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.xfy9326.glance.ui.data.ImageObject
import io.github.xfy9326.glance.ui.data.ImageObjectInfo
import io.github.xfy9326.glance.ui.data.calculateLayout
import io.github.xfy9326.glance.ui.theme.AppTheme

val PreviewImageObjectInfo = ImageObjectInfo(
    size = Size(500f, 500f),
    objects = listOf(
        ImageObject(0, "class_1", 100, Offset(0f, 0f), Size(50f, 50f)),
        ImageObject(0, "class_1", 90, Offset(450f, 0f), Size(50f, 50f)),
        ImageObject(2, "class_3", 80, Offset(450f, 450f), Size(50f, 50f)),
        ImageObject(2, "class_3", 70, Offset(0f, 450f), Size(50f, 50f)),
        ImageObject(4, "class_5", 60, Offset(100f, 100f), Size(300f, 300f)),
        ImageObject(5, "class_6", 50, Offset(0f, 200f), Size(500f, 100f)),
        ImageObject(6, "class_7", 40, Offset(200f, 0f), Size(100f, 500f))
    )
)

@Preview(showBackground = true, widthDp = 500, heightDp = 500)
@Composable
private fun PreviewImageObjectBoxLayer() {
    AppTheme {
        ImageObjectBoxLayer(
            imageObjectInfo = PreviewImageObjectInfo,
            modifier = Modifier.size(500.dp),
        )
    }
}

@Composable
fun ImageObjectBoxLayer(
    imageObjectInfo: ImageObjectInfo,
    modifier: Modifier,
    boxStroke: Dp = 2.dp,
    boxStrokeColor: Color = Color.Red,
    cornerRadius: Dp = 2.dp,
) {
    val targetBoxStroke = if (boxStroke == Dp.Hairline) {
        (1f / LocalDensity.current.density).dp
    } else {
        boxStroke
    }
    Canvas(
        modifier = modifier,
        onDraw = {
            val stroke = Stroke(width = targetBoxStroke.toPx())
            val radius = CornerRadius(cornerRadius.toPx(), cornerRadius.toPx())
            imageObjectInfo.calculateLayout(size).forEach {
                drawRoundRect(
                    color = boxStrokeColor,
                    topLeft = it.boxOffset,
                    size = it.boxSize,
                    style = stroke,
                    cornerRadius = radius
                )
            }
        }
    )
}

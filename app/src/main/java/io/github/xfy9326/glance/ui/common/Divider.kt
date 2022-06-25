package io.github.xfy9326.glance.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private const val DividerAlpha = 0.12f

enum class DividerDirection {
    Vertical,
    Horizontal
}

@Composable
fun Divider(
    direction: DividerDirection,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.onSurface.copy(alpha = DividerAlpha),
    thickness: Dp = 1.dp,
    startIndent: Dp = 0.dp,
) {
    val indentMod = if (startIndent.value != 0f) {
        Modifier.padding(start = startIndent)
    } else {
        Modifier
    }
    val targetThickness = if (thickness == Dp.Hairline) {
        (1f / LocalDensity.current.density).dp
    } else {
        thickness
    }
    Box(
        modifier
            .then(indentMod)
            .let {
                when (direction) {
                    DividerDirection.Horizontal -> it
                        .fillMaxWidth()
                        .height(targetThickness)
                    DividerDirection.Vertical -> it
                        .fillMaxHeight()
                        .width(targetThickness)
                }
            }
            .background(color = color)
    )
}

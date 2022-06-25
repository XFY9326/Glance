package io.github.xfy9326.glance.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = AppThemeColor,
        content = content,
        shapes = AppThemeShape
    )
}
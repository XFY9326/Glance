package io.github.xfy9326.glance.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import io.github.xfy9326.glance.R

object AppTheme {
    private val darkColors = darkColors()

    private val lightColors = lightColors(
        background = Color.White
    )

    val themeColor: Colors
        @Composable
        get() = if (isSystemInDarkTheme()) darkColors else lightColors

    val launcherIconColor: Color
        @Composable
        get() = if (isSystemInDarkTheme()) Color.White else colorResource(id = R.color.ic_launcher_foreground)
}

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = AppTheme.themeColor,
        content = content
    )
}
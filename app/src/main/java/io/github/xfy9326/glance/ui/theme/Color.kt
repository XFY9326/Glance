package io.github.xfy9326.glance.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import io.github.xfy9326.glance.R

private val darkColors = darkColors()

private val lightColors = lightColors(
    surface = Color.White
)

val AppThemeColor: Colors
    @Composable
    get() = if (isSystemInDarkTheme()) darkColors else lightColors

val Colors.topAppBarColor: Color
    @Composable
    get() = surface

val Colors.launcherIconColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) surface else colorResource(id = R.color.ic_launcher_foreground)



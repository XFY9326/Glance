package io.github.xfy9326.glance.ui.base

import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import io.github.xfy9326.glance.R
import io.github.xfy9326.glance.ui.theme.AppTheme

@Composable
fun ApplicationIcon(modifier: Modifier) {
    Icon(
        imageVector = ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
        contentDescription = null,
        modifier = modifier,
        tint = AppTheme.launcherIconColor
    )
}
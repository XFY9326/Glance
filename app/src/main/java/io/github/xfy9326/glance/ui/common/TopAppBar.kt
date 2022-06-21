package io.github.xfy9326.glance.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import io.github.xfy9326.glance.ui.theme.AppThemeColor
import io.github.xfy9326.glance.ui.theme.topAppBarColor

@Composable
fun TopAppToolBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    backgroundColor: Color = AppThemeColor.topAppBarColor,
    contentColor: Color = contentColorFor(backgroundColor),
    elevation: Dp = AppBarDefaults.TopAppBarElevation,
) {
    com.google.accompanist.insets.ui.TopAppBar(
        title = title,
        modifier = modifier,
        contentPadding = WindowInsets.statusBars.only(
            WindowInsetsSides.Horizontal + WindowInsetsSides.Top
        ).asPaddingValues(),
        navigationIcon = navigationIcon,
        actions = actions,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        elevation = elevation
    )
}
package io.github.xfy9326.glance.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import io.github.xfy9326.glance.R
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

@Composable
fun SimpleTopAppToolBar(
    title: String,
    onBackPressed: () -> Unit
) {
    TopAppToolBar(
        title = {
            Text(text = title)
        },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.back)
                )
            }
        }
    )
}
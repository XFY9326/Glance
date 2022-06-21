package io.github.xfy9326.glance.ui.screen.guide.composable

import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable

@Composable
fun GuideScreen(
    onBackPressed: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    GuideContent(
        scaffoldState = scaffoldState,
        onBackPressed = onBackPressed
    )
}

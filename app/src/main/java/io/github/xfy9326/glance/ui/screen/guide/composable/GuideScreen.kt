package io.github.xfy9326.glance.ui.screen.guide.composable

import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import io.github.xfy9326.glance.ui.screen.guide.GuideViewModel

@Composable
fun GuideScreen(
    viewModel: GuideViewModel,
    onBackPressed: () -> Unit,
) {
    val scaffoldState = rememberScaffoldState()

    GuideContent(
        scaffoldState = scaffoldState,
        onBackPressed = onBackPressed
    )
}

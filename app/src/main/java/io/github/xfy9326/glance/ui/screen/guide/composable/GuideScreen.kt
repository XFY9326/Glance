package io.github.xfy9326.glance.ui.screen.guide.composable

import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import io.github.xfy9326.glance.ui.base.PreviewSurfaceProvider
import io.github.xfy9326.glance.ui.screen.guide.GuideViewModel

@Composable
fun GuideScreen(
    viewModel: GuideViewModel,
    onBackPressed: () -> Unit,
    onBindCamera: (PreviewSurfaceProvider) -> Unit,
) {
    val scaffoldState = rememberScaffoldState()
    val analysisResult = viewModel.analysisResult.collectAsState()

    GuideContent(
        scaffoldState = scaffoldState,
        onBackPressed = onBackPressed,
        onBindCamera = onBindCamera,
        analysisResult = analysisResult.value
    )
}



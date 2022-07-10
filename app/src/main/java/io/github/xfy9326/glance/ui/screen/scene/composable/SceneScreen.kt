package io.github.xfy9326.glance.ui.screen.scene.composable

import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import io.github.xfy9326.glance.ui.base.PreviewSurfaceProvider
import io.github.xfy9326.glance.ui.screen.scene.SceneViewModel

@Composable
fun SceneScreen(
    viewModel: SceneViewModel,
    onBackPressed: () -> Unit,
    onBindCamera: (PreviewSurfaceProvider) -> Unit,
) {
    val scaffoldState = rememberScaffoldState()
    val analysisResult = viewModel.analysisResult.collectAsState()

    SceneContent(
        scaffoldState = scaffoldState,
        onBackPressed = onBackPressed,
        onBindCamera = onBindCamera,
        analysisResult = analysisResult.value
    )
}

package io.github.xfy9326.glance.ui.screen.guide.composable

import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.xfy9326.glance.ui.base.toPixelsData
import io.github.xfy9326.glance.ui.screen.guide.GuideViewModel

@Composable
fun GuideScreen(
    viewModel: GuideViewModel = viewModel(),
    onBackPressed: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val analysisResult = viewModel.analysisResult.collectAsState()

    GuideContent(
        scaffoldState = scaffoldState,
        onBackPressed = onBackPressed,
        imageAnalyzer = {
            viewModel.detectImage(it.toPixelsData())
            it.close()
        },
        analysisResult = analysisResult.value
    )
}

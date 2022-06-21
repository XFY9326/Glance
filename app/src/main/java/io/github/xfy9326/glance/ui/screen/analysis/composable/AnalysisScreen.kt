package io.github.xfy9326.glance.ui.screen.analysis.composable

import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import io.github.xfy9326.glance.ui.screen.analysis.AnalysisViewModel

@Composable
fun AnalysisScreen(
    viewModel: AnalysisViewModel,
    onBackPressed: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val analyzeText = viewModel.analyzeText.collectAsState()

    AnalysisScreenContent(
        scaffoldState = scaffoldState,
        imageUri = viewModel.imageUri,
        analyzeText = analyzeText.value,
        onBackPressed = onBackPressed
    )
}

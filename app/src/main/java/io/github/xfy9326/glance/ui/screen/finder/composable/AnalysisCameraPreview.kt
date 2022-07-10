package io.github.xfy9326.glance.ui.screen.finder.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.xfy9326.glance.ui.base.PreviewSurfaceProvider
import io.github.xfy9326.glance.ui.common.CameraPreview
import io.github.xfy9326.glance.ui.common.ImageObjectBoxLayer
import io.github.xfy9326.glance.ui.data.AnalysisResult

@Composable
fun AnalysisCameraPreview(
    modifier: Modifier,
    analysisResult: AnalysisResult,
    onBindCamera: (PreviewSurfaceProvider) -> Unit,
) {
    Box(modifier = modifier) {
        CameraPreview(
            modifier = Modifier.fillMaxSize(),
            onBindCamera = onBindCamera
        )
        if (analysisResult is AnalysisResult.DetectSuccess) {
            ImageObjectBoxLayer(
                imageObjectInfo = analysisResult.imageObjectInfo,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
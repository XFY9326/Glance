package io.github.xfy9326.glance.ui.screen.finder.composable

import android.view.ViewGroup
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import io.github.xfy9326.glance.ui.base.PreviewSurfaceProvider
import io.github.xfy9326.glance.ui.common.ImageObjectBoxLayer
import io.github.xfy9326.glance.ui.data.AnalysisResult

@Composable
fun FinderCameraPreview(
    modifier: Modifier,
    analysisResult: AnalysisResult,
    onBindCamera: (PreviewSurfaceProvider) -> Unit,
) {
    Box(modifier = modifier) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                PreviewView(context).apply {
                    scaleType = PreviewView.ScaleType.FIT_CENTER
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }.also {
                    onBindCamera(it.surfaceProvider)
                }
            }
        )
        if (analysisResult is AnalysisResult.Success) {
            ImageObjectBoxLayer(
                imageObjectInfo = analysisResult.imageObjectInfo,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
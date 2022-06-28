package io.github.xfy9326.glance.ui.screen.guide.composable

import android.view.ViewGroup
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import io.github.xfy9326.glance.ui.base.PreviewSurfaceProvider

@Composable
fun GuideCameraPreview(
    modifier: Modifier,
    onBindCamera: (PreviewSurfaceProvider) -> Unit,
) {
    AndroidView(
        modifier = modifier,
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
}
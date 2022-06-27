package io.github.xfy9326.glance.ui.screen.guide.composable

import android.view.ViewGroup
import androidx.camera.core.AspectRatio
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import io.github.xfy9326.glance.ui.base.CameraPreviewBuilder
import io.github.xfy9326.glance.ui.base.PreviewUseCase

@Composable
fun GuideCameraPreview(
    modifier: Modifier,
    onBindCamera: (PreviewUseCase) -> Unit,
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
                val previewUseCase = CameraPreviewBuilder().setTargetAspectRatio(AspectRatio.RATIO_4_3).build()
                previewUseCase.setSurfaceProvider(it.surfaceProvider)
                onBindCamera(previewUseCase)
            }
        }
    )
}
package io.github.xfy9326.glance.utils

import androidx.activity.ComponentActivity
import androidx.camera.core.AspectRatio
import androidx.camera.core.ImageAnalysis
import androidx.lifecycle.lifecycleScope
import io.github.xfy9326.glance.ui.base.*
import kotlinx.coroutines.launch

object AnalysisCameraUtils {
    fun ComponentActivity.bindAnalysisCamera(
        surfaceProvider: PreviewSurfaceProvider,
        onSetImageAnalysisAnalyzer: (ImageAnalysis) -> Unit
    ) {
        lifecycleScope.launch {
            val cameraProvider = getCameraProvider()
            val cameraSelector = cameraProvider.getDefaultCameraSelector()
            val imageAnalysisUseCase = buildImageAnalysisUseCase(onSetImageAnalysisAnalyzer)
            val previewUseCase = buildPreviewUseCase(surfaceProvider)
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this@bindAnalysisCamera, cameraSelector, previewUseCase, imageAnalysisUseCase)
            } catch (e: Exception) {
                // Ignore
            }
        }
    }

    private fun buildPreviewUseCase(surfaceProvider: PreviewSurfaceProvider): PreviewUseCase {
        val previewUseCase = CameraPreviewBuilder()
            .setTargetAspectRatio(AspectRatio.RATIO_4_3)
            .build()
        previewUseCase.setSurfaceProvider(surfaceProvider)
        return previewUseCase
    }

    private fun buildImageAnalysisUseCase(onSetImageAnalysisAnalyzer: (ImageAnalysis) -> Unit): ImageAnalysis {
        val imageAnalysisUseCase = ImageAnalysis.Builder()
            .setTargetAspectRatio(AspectRatio.RATIO_4_3)
            .setOutputImageRotationEnabled(true)
            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
            .setImageQueueDepth(1)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
        onSetImageAnalysisAnalyzer(imageAnalysisUseCase)
        return imageAnalysisUseCase
    }
}
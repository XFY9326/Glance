package io.github.xfy9326.glance.activity

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.camera.core.AspectRatio
import androidx.camera.core.ImageAnalysis
import androidx.compose.runtime.Composable
import androidx.lifecycle.lifecycleScope
import io.github.xfy9326.atools.core.isPermissionGranted
import io.github.xfy9326.atools.core.showToast
import io.github.xfy9326.glance.R
import io.github.xfy9326.glance.ui.base.*
import io.github.xfy9326.glance.ui.screen.finder.FinderViewModel
import io.github.xfy9326.glance.ui.screen.finder.composable.FinderScreen
import io.github.xfy9326.glance.ui.theme.AppTheme
import kotlinx.coroutines.launch

class FinderActivity : ComponentActivity() {
    private val viewModel by viewModels<FinderViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!isPermissionGranted(Manifest.permission.CAMERA)) {
            showToast(getString(R.string.permission_not_granted, getString(R.string.camera_permission)))
            finish()
        } else {
            setContent {
                AppTheme {
                    Content()
                }
            }
        }
    }

    @Composable
    private fun Content() {
        FinderScreen(
            viewModel = viewModel,
            onBackPressed = this::onBackPressed,
            onBindCamera = this::onBindCamera
        )
    }

    private fun onBindCamera(surfaceProvider: PreviewSurfaceProvider) {
        lifecycleScope.launch {
            val cameraProvider = getCameraProvider()
            val cameraSelector = cameraProvider.getDefaultCameraSelector()
            val imageAnalysisUseCase = onBuildImageAnalysisUseCase()
            val previewUseCase = onBuildPreviewUseCase(surfaceProvider)
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this@FinderActivity, cameraSelector, previewUseCase, imageAnalysisUseCase)
            } catch (e: Exception) {
                // Ignore
            }
        }
    }

    private fun onBuildPreviewUseCase(surfaceProvider: PreviewSurfaceProvider): PreviewUseCase {
        val previewUseCase = CameraPreviewBuilder()
            .setTargetAspectRatio(AspectRatio.RATIO_4_3)
            .build()
        previewUseCase.setSurfaceProvider(surfaceProvider)
        return previewUseCase
    }

    private fun onBuildImageAnalysisUseCase(): ImageAnalysis {
        val imageAnalysisUseCase = ImageAnalysis.Builder()
            .setTargetAspectRatio(AspectRatio.RATIO_4_3)
            .setOutputImageRotationEnabled(true)
            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
            .setImageQueueDepth(1)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
        viewModel.setImageAnalysisAnalyzer(imageAnalysisUseCase)
        return imageAnalysisUseCase
    }
}
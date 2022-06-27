package io.github.xfy9326.glance.activity

import android.Manifest
import android.os.Bundle
import android.view.Surface
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
import io.github.xfy9326.glance.ui.base.PreviewUseCase
import io.github.xfy9326.glance.ui.base.getCameraProvider
import io.github.xfy9326.glance.ui.base.getDefaultCameraSelector
import io.github.xfy9326.glance.ui.screen.guide.GuideViewModel
import io.github.xfy9326.glance.ui.screen.guide.composable.GuideScreen
import io.github.xfy9326.glance.ui.theme.AppTheme
import kotlinx.coroutines.launch

class GuideActivity : ComponentActivity() {
    private val viewModel by viewModels<GuideViewModel>()

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
        GuideScreen(
            viewModel = viewModel,
            onBackPressed = this::onBackPressed,
            onBindCamera = this::onBindCamera
        )
    }

    private fun onBindCamera(previewUseCase: PreviewUseCase) {
        lifecycleScope.launch {
            val cameraProvider = getCameraProvider()
            val cameraSelector = cameraProvider.getDefaultCameraSelector()
            val imageAnalysisUseCase = onBuildImageAnalysisUseCase()
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this@GuideActivity, cameraSelector, imageAnalysisUseCase, previewUseCase)
            } catch (e: Exception) {
                // Ignore
            }
        }
    }

    private fun onBuildImageAnalysisUseCase(): ImageAnalysis {
        val imageAnalysisUseCase = ImageAnalysis.Builder()
            .setTargetAspectRatio(AspectRatio.RATIO_4_3)
            .setTargetRotation(Surface.ROTATION_0)
            .setOutputImageRotationEnabled(true)
            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
            .setImageQueueDepth(1)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
        viewModel.setImageAnalysisAnalyzer(imageAnalysisUseCase)
        return imageAnalysisUseCase
    }
}
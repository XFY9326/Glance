package io.github.xfy9326.glance.activity

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import io.github.xfy9326.atools.core.isPermissionGranted
import io.github.xfy9326.atools.core.showToast
import io.github.xfy9326.glance.R
import io.github.xfy9326.glance.ui.screen.finder.FinderViewModel
import io.github.xfy9326.glance.ui.screen.finder.composable.FinderScreen
import io.github.xfy9326.glance.ui.theme.AppTheme
import io.github.xfy9326.glance.utils.AnalysisCameraUtils.bindAnalysisCamera

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
            onBindCamera = {
                bindAnalysisCamera(it, viewModel::setImageAnalysisAnalyzer)
            }
        )
    }
}
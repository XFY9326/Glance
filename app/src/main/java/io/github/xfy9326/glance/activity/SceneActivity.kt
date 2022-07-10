package io.github.xfy9326.glance.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import io.github.xfy9326.glance.ui.screen.scene.SceneViewModel
import io.github.xfy9326.glance.ui.screen.scene.composable.SceneScreen
import io.github.xfy9326.glance.ui.theme.AppTheme
import io.github.xfy9326.glance.utils.AnalysisCameraUtils.bindAnalysisCamera

class SceneActivity : ComponentActivity() {
    private val viewModel by viewModels<SceneViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Content()
            }
        }
    }

    @Composable
    private fun Content() {
        SceneScreen(
            viewModel = viewModel,
            onBackPressed = this::onBackPressed,
            onBindCamera = {
                bindAnalysisCamera(it, viewModel::setImageAnalysisAnalyzer)
            }
        )
    }
}
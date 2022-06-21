package io.github.xfy9326.glance.activity

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import io.github.xfy9326.glance.ui.screen.analysis.AnalysisViewModel
import io.github.xfy9326.glance.ui.screen.analysis.AnalysisViewModelFactory
import io.github.xfy9326.glance.ui.screen.analysis.composable.AnalysisScreen
import io.github.xfy9326.glance.ui.theme.AppTheme

class AnalysisActivity : ComponentActivity() {
    private val imageUri: Uri by lazy { intent?.data ?: error("No data in intent!") }
    private val viewModel by viewModels<AnalysisViewModel> { AnalysisViewModelFactory(imageUri) }

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
        AnalysisScreen(
            viewModel = viewModel,
            onBackPressed = {
                finish()
            }
        )
    }
}
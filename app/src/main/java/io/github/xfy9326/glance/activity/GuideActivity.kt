package io.github.xfy9326.glance.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import io.github.xfy9326.glance.ui.screen.guide.GuideViewModel
import io.github.xfy9326.glance.ui.screen.guide.composable.GuideScreen
import io.github.xfy9326.glance.ui.theme.AppTheme

class GuideActivity : ComponentActivity() {
    private val viewModel by viewModels<GuideViewModel>()

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
        GuideScreen(
            viewModel = viewModel,
            onBackPressed = this::onBackPressed
        )
    }
}
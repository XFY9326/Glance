package io.github.xfy9326.glance.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import io.github.xfy9326.glance.App
import io.github.xfy9326.glance.tools.startActivity
import io.github.xfy9326.glance.ui.screen.main.composable.MainScreen
import io.github.xfy9326.glance.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setupSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Content()
            }
        }
    }

    private fun setupSplashScreen() =
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !App.isAppInitialized
            }
            setOnExitAnimationListener {
                it.view.animate().alpha(0f).withEndAction {
                    it.remove()
                }.start()
            }
        }

    @Composable
    private fun Content() {
        val context = LocalContext.current
        MainScreen(
            onNavigateToGuide = {
                context.startActivity<GuideActivity>()
            }, onNavigateToAnalysis = {
                context.startActivity<AnalysisActivity> { data = it }
            }
        )
    }
}

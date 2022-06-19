package io.github.xfy9326.glance.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import io.github.xfy9326.glance.ui.activity.MainActivityContent
import io.github.xfy9326.glance.ui.base.prepareSplashScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        prepareSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            MainActivityContent()
        }
    }
}

package io.github.xfy9326.glance.ui.base

import android.app.Activity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import io.github.xfy9326.glance.App

fun Activity.prepareSplashScreen() =
    installSplashScreen().setKeepOnScreenCondition {
        !App.isAppInitialized
    }
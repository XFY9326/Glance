package io.github.xfy9326.glance.ui.base

import android.app.Activity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

fun Activity.prepareSplashScreen() =
    installSplashScreen().setOnExitAnimationListener {
        it.view.animate().alpha(0f)
    }
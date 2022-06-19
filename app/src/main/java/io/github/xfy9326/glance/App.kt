package io.github.xfy9326.glance

import android.app.Application
import io.github.xfy9326.glance.ml.MLManager

class App : Application() {
    companion object {
        private var internalIsAppInitialized: Boolean = false
        val isAppInitialized: Boolean
            get() = internalIsAppInitialized
    }

    override fun onCreate() {
        super.onCreate()
        MLManager.initModelsInBackground()
        internalIsAppInitialized = true
    }
}
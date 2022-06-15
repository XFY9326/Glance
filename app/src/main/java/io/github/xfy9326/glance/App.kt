package io.github.xfy9326.glance

import android.app.Application
import io.github.xfy9326.glance.ml.MLManager
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class App : Application() {
    companion object {
        private var internalIsAppInitialized: Boolean = false
        val isAppInitialized: Boolean
            get() = internalIsAppInitialized
    }

    override fun onCreate() {
        super.onCreate()
        init()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun init() {
        GlobalScope.launch(Dispatchers.Default) {
            MLManager.initInstance()
            internalIsAppInitialized = true
            MLManager.initModelsInBackground()
        }
    }
}
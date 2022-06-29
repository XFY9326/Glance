@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package io.github.xfy9326.glance.ml

import io.github.xfy9326.glance.ml.model.DetectionModel
import io.github.xfy9326.glance.ml.model.base.AbstractDetectionModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

object MLManager {
    private var initMutex = Mutex()
    private var hasModelInitialized = false

    @OptIn(DelicateCoroutinesApi::class)
    fun initModelsInBackground() {
        if (!hasModelInitialized) {
            GlobalScope.launch(Dispatchers.IO) {
                initMutex.withLock {
                    if (!hasModelInitialized) {
                        hasModelInitialized = true
                        DetectionModel.init()
                    }
                }
            }
        }
    }

    fun isGPUInstanceCreated(): Boolean =
        NativeInterface.isGPUInstanceCreated()

    fun hasGPUSupport(): Boolean =
        NativeInterface.hasGPUSupport()

    fun getDetectionModel(): AbstractDetectionModel =
        DetectionModel
}
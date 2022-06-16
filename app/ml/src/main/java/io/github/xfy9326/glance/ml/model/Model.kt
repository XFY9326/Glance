package io.github.xfy9326.glance.ml.model

import android.graphics.Bitmap
import io.github.xfy9326.glance.ml.beans.DetectObject
import io.github.xfy9326.glance.ml.beans.PixelsData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

abstract class Model {
    private val initMutex = Mutex()
    private val detectMutex = Mutex()
    private var initSuccess: Boolean? = null

    protected abstract fun onInitModel(): Boolean

    abstract fun isInitialized(): Boolean

    protected abstract fun onDetectByBitmap(bitmap: Bitmap, enableGPU: Boolean): Array<DetectObject>?

    protected abstract fun onDetectByPixelsData(pixelsData: PixelsData, enableGPU: Boolean): Array<DetectObject>?

    private suspend fun internalInit(): Boolean {
        return initSuccess ?: initMutex.withLock {
            initSuccess ?: onInitModel().also {
                initSuccess = it
            }
        }
    }

    internal suspend fun init() = with(CoroutineScope(coroutineContext)) {
        launch {
            internalInit()
        }
    }

    suspend fun withInitializedModel(block: suspend (Boolean) -> Unit) {
        block(internalInit())
    }

    suspend fun detectByBitmap(bitmap: Bitmap, enableGPU: Boolean): Array<DetectObject>? = withContext(Dispatchers.Default) {
        if (initMutex.withLock { initSuccess == true }) {
            detectMutex.withLock {
                onDetectByBitmap(bitmap, enableGPU)
            }
        } else {
            null
        }
    }

    suspend fun detectByPixelsData(pixelsData: PixelsData, enableGPU: Boolean): Array<DetectObject>? = withContext(Dispatchers.Default) {
        if (initMutex.withLock { initSuccess == true }) {
            detectMutex.withLock {
                onDetectByPixelsData(pixelsData, enableGPU)
            }
        } else {
            null
        }
    }
}
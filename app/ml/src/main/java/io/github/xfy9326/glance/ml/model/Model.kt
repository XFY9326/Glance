package io.github.xfy9326.glance.ml.model

import android.graphics.Bitmap
import io.github.xfy9326.glance.ml.beans.*
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

    protected abstract fun onDetectByBitmap(bitmap: Bitmap, confThreshold: Float, iouThreshold: Float): Array<DetectObject>?

    protected abstract fun onDetectByPixelsData(pixelsData: PixelsData, confThreshold: Float, iouThreshold: Float): Array<DetectObject>?

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

    private suspend fun detect(width: Int, height: Int, block: () -> Array<DetectObject>?): DetectResult =
        withContext(Dispatchers.Default) {
            if (internalInit()) {
                val result = detectMutex.withLock {
                    block()
                }
                if (result != null) {
                    return@withContext DetectResult.Success(
                        DetectInfo(
                            width = width,
                            height = height,
                            objects = result
                        )
                    )
                }
            }
            return@withContext DetectResult.ModelInitFailed
        }

    suspend fun detectByBitmap(bitmap: Bitmap, confThreshold: MLThreshold.Confidence, iouThreshold: MLThreshold.IOU): DetectResult =
        detect(bitmap.width, bitmap.height) {
            onDetectByBitmap(bitmap, confThreshold.value, iouThreshold.value)
        }

    suspend fun detectByPixelsData(pixelsData: PixelsData, confThreshold: MLThreshold.Confidence, iouThreshold: MLThreshold.IOU): DetectResult =
        detect(pixelsData.width, pixelsData.height) {
            onDetectByPixelsData(pixelsData, confThreshold.value, iouThreshold.value)
        }
}
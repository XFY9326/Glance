package io.github.xfy9326.glance.ml.model.base

import android.graphics.Bitmap
import io.github.xfy9326.glance.ml.beans.DetectInfo
import io.github.xfy9326.glance.ml.beans.DetectObject
import io.github.xfy9326.glance.ml.beans.PixelsData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.IOException

abstract class AbstractDetectionModel : AbstractModel() {
    private val detectMutex = Mutex()

    @Throws(IOException::class)
    protected abstract fun onLoadLabels(): Array<String>

    protected abstract fun onDetectByBitmap(bitmap: Bitmap, confThreshold: Float, iouThreshold: Float): Array<DetectObject>?

    protected abstract fun onDetectByPixelsData(pixelsData: PixelsData, confThreshold: Float, iouThreshold: Float): Array<DetectObject>?

    private suspend fun detect(width: Int, height: Int, block: () -> Array<DetectObject>?): Result<DetectInfo> =
        withContext(Dispatchers.Default) {
            runCatching {
                if (internalInit()) {
                    val result = detectMutex.withLock {
                        block()
                    }
                    if (result != null) {
                        return@runCatching DetectInfo(
                            width = width,
                            height = height,
                            objects = result
                        )
                    }
                }
                error("Model init failed!")
            }
        }

    suspend fun loadLabels(): Result<Array<String>> = withContext(Dispatchers.IO) {
        runCatching {
            onLoadLabels()
        }
    }

    suspend fun detectByBitmap(bitmap: Bitmap, confThreshold: Float, iouThreshold: Float): Result<DetectInfo> =
        detect(bitmap.width, bitmap.height) {
            onDetectByBitmap(bitmap, confThreshold, iouThreshold)
        }

    suspend fun detectByPixelsData(pixelsData: PixelsData, confThreshold: Float, iouThreshold: Float): Result<DetectInfo> =
        detect(pixelsData.width, pixelsData.height) {
            onDetectByPixelsData(pixelsData, confThreshold, iouThreshold)
        }
}
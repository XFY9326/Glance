package io.github.xfy9326.glance.ui.base

import android.content.Context
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import io.github.xfy9326.glance.ml.beans.PixelsData
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

typealias CameraPreviewBuilder = androidx.camera.core.Preview.Builder

suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { future ->
        future.addListener({
            continuation.resume(future.get())
        }, ContextCompat.getMainExecutor(this))
    }
}

// Only valid for JPEG format image
fun ImageProxy.toPixelsData() = PixelsData(
    width = width,
    height = height,
    stride = planes.first().rowStride,
    pixels = planes.first().buffer
)

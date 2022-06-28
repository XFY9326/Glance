package io.github.xfy9326.glance.ui.base

import android.content.Context
import androidx.camera.core.CameraProvider
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import io.github.xfy9326.glance.ml.beans.PixelsData
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

typealias PreviewUseCase = Preview
typealias PreviewSurfaceProvider = Preview.SurfaceProvider
typealias CameraPreviewBuilder = Preview.Builder

fun CameraProvider.getDefaultCameraSelector() =
    if (hasCamera(CameraSelector.DEFAULT_BACK_CAMERA)) {
        CameraSelector.DEFAULT_BACK_CAMERA
    } else {
        CameraSelector.DEFAULT_FRONT_CAMERA
    }

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

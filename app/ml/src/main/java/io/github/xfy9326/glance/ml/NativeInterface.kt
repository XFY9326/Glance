package io.github.xfy9326.glance.ml

import android.content.res.AssetManager
import android.graphics.Bitmap
import io.github.xfy9326.glance.ml.beans.DetectObject
import io.github.xfy9326.glance.ml.beans.PixelsData

internal object NativeInterface {
    init {
        System.loadLibrary("ml")
    }

    external fun isGPUInstanceCreated(): Boolean

    external fun hasGPUSupport(): Boolean

    external fun initDetectionModel(assetManager: AssetManager, binPath: String, paramBinPath: String): Boolean

    external fun isDetectionModelInitialized(): Boolean

    external fun detectByPixelsData(pixelsData: PixelsData, confThreshold: Float, iouThreshold: Float): Array<DetectObject>?

    external fun detectByBitmap(bitmap: Bitmap, confThreshold: Float, iouThreshold: Float): Array<DetectObject>?
}
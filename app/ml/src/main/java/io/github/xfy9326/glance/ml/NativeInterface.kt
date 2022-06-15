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

    external fun isModelInitialized(modelType: Int): Boolean

    external fun initModel(modelType: Int, assetManager: AssetManager, binPath: String, paramBinPath: String): Boolean

    external fun detectByPixelsData(modelType: Int, pixelsData: PixelsData, enableGPU: Boolean, confThreshold: Float, iouThreshold: Float): Array<DetectObject>?

    external fun detectByBitmap(modelType: Int, bitmap: Bitmap, enableGPU: Boolean, confThreshold: Float, iouThreshold: Float): Array<DetectObject>?
}
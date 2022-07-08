package io.github.xfy9326.glance.ml

import android.content.res.AssetManager
import android.graphics.Bitmap
import io.github.xfy9326.glance.ml.beans.MLOutput
import io.github.xfy9326.glance.ml.beans.PixelsData

internal object NativeInterface {
    init {
        System.loadLibrary("ml")
    }

    external fun isGPUInstanceCreated(): Boolean

    external fun hasGPUSupport(): Boolean

    external fun initModels(assetManager: AssetManager): Boolean

    external fun isModelsInitialized(): Boolean

    external fun analyzeImageByPixelsData(
        pixelsData: PixelsData, confThreshold: Float, iouThreshold: Float, requestCaption: Boolean
    ): MLOutput

    external fun analyzeImageByBitmap(
        bitmap: Bitmap, confThreshold: Float, iouThreshold: Float, requestCaption: Boolean
    ): MLOutput
}
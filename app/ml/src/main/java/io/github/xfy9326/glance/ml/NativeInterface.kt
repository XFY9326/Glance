package io.github.xfy9326.glance.ml

import android.content.res.AssetManager
import android.graphics.Bitmap
import io.github.xfy9326.glance.ml.beans.MLDetectOutput
import io.github.xfy9326.glance.ml.beans.PixelsData

internal object NativeInterface {
    init {
        System.loadLibrary("ml")
    }

    external fun isGPUInstanceCreated(): Boolean

    external fun hasGPUSupport(): Boolean

    external fun initModels(assetManager: AssetManager): Boolean

    external fun isModelsInitialized(): Boolean

    external fun analyzeImageCaptionByPixelsData(pixelsData: PixelsData): IntArray?

    external fun analyzeImageByPixelsData(
        pixelsData: PixelsData, confThreshold: Float, iouThreshold: Float, requestCaption: Boolean
    ): MLDetectOutput

    external fun analyzeImageByBitmap(
        bitmap: Bitmap, confThreshold: Float, iouThreshold: Float, requestCaption: Boolean
    ): MLDetectOutput
}
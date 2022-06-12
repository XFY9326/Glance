package io.github.xfy9326.glance.ml

import android.content.res.AssetManager
import android.graphics.Bitmap
import io.github.xfy9326.glance.ml.beans.DetectObject
import io.github.xfy9326.glance.ml.beans.PixelsData

internal object NativeInterface {
    var hasInitSuccess = false
        private set

    init {
        try {
            System.loadLibrary("ml")
            hasInitSuccess = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Synchronized
    external fun isGPUInstanceCreated(): Boolean

    @Synchronized
    external fun hasGPUSupport(): Boolean

    @Synchronized
    external fun isGuideModelInitialized(): Boolean

    @Synchronized
    external fun isGeneralModelInitialized(): Boolean

    @Synchronized
    external fun initGuideModel(assetManager: AssetManager, binPath: String, paramBinPath: String): Boolean

    @Synchronized
    external fun initGeneralModel(assetManager: AssetManager, binPath: String, paramBinPath: String): Boolean

    @Synchronized
    external fun detectGuideModel(pixelsData: PixelsData, enableGPU: Boolean, confThreshold: Float, iouThreshold: Float): Array<DetectObject>?

    @Synchronized
    external fun detectGeneralModel(bitmap: Bitmap, enableGPU: Boolean, confThreshold: Float, iouThreshold: Float): Array<DetectObject>?
}
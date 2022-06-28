package io.github.xfy9326.glance.ml.model

import android.graphics.Bitmap
import io.github.xfy9326.atools.io.IOManager
import io.github.xfy9326.glance.ml.NativeInterface
import io.github.xfy9326.glance.ml.beans.DetectObject
import io.github.xfy9326.glance.ml.beans.ModelType
import io.github.xfy9326.glance.ml.beans.PixelsData

internal abstract class NativeModel(private val modelType: ModelType) : Model() {

    override fun onInitModel(): Boolean =
        NativeInterface.initModel(modelType.nativeTypeInt, IOManager.assetManager, modelType.binPath, modelType.paramBinPath)

    override fun isInitialized(): Boolean =
        NativeInterface.isModelInitialized(modelType.nativeTypeInt)

    override fun onDetectByBitmap(bitmap: Bitmap, confThreshold: Float, iouThreshold: Float): Array<DetectObject>? =
        NativeInterface.detectByBitmap(modelType.nativeTypeInt, bitmap, confThreshold, iouThreshold)

    override fun onDetectByPixelsData(pixelsData: PixelsData, confThreshold: Float, iouThreshold: Float): Array<DetectObject>? =
        NativeInterface.detectByPixelsData(modelType.nativeTypeInt, pixelsData, confThreshold, iouThreshold)
}
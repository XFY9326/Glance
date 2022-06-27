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

    override fun onDetectByBitmap(bitmap: Bitmap): Array<DetectObject>? =
        NativeInterface.detectByBitmap(modelType.nativeTypeInt, bitmap, modelType.confThreshold, modelType.iouThreshold)

    override fun onDetectByPixelsData(pixelsData: PixelsData): Array<DetectObject>? =
        NativeInterface.detectByPixelsData(modelType.nativeTypeInt, pixelsData, modelType.confThreshold, modelType.iouThreshold)
}
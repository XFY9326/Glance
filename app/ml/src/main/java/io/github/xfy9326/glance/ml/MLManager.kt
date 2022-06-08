package io.github.xfy9326.glance.ml

import android.graphics.Bitmap
import io.github.xfy9326.atools.io.IOManager
import io.github.xfy9326.glance.ml.beans.DetectObject
import io.github.xfy9326.glance.ml.beans.PixelsData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object MLManager {

    fun isGPUInstanceCreated(): Boolean =
        NativeInterface.isGPUInstanceCreated()

    fun hasGPUSupport(): Boolean =
        NativeInterface.hasGPUSupport()

    fun isGuideModelInitialized(): Boolean =
        NativeInterface.isGeneralModelInitialized()

    fun isGeneralModelInitialized(): Boolean =
        NativeInterface.isGeneralModelInitialized()

    suspend fun initGuideModel(): Boolean = withContext(Dispatchers.IO) {
        NativeInterface.initGuideModel(IOManager.assetManager, MLConfig.MODEL_GUIDE_BIN_PATH, MLConfig.MODEL_GUIDE_PARAM_BIN_PATH)
    }

    suspend fun initGeneralModel(): Boolean = withContext(Dispatchers.IO) {
        NativeInterface.initGuideModel(IOManager.assetManager, MLConfig.MODEL_GENERAL_BIN_PATH, MLConfig.MODEL_GENERAL_PARAM_BIN_PATH)
    }

    suspend fun detectGuideModel(pixelsData: PixelsData, enableGPU: Boolean): Array<DetectObject>? = withContext(Dispatchers.Default) {
        NativeInterface.detectGuideModel(pixelsData, enableGPU, MLConfig.DEFAULT_CONF_THRESHOLD, MLConfig.DEFAULT_IOU_THRESHOLD)
    }

    suspend fun detectGeneralModel(bitmap: Bitmap, enableGPU: Boolean): Array<DetectObject>? = withContext(Dispatchers.Default) {
        NativeInterface.detectGeneralModel(bitmap, enableGPU, MLConfig.DEFAULT_CONF_THRESHOLD, MLConfig.DEFAULT_IOU_THRESHOLD)
    }

    suspend fun loadGuideLabels(): Array<String> {
        return LabelsUtils.loadLabels(R.raw.labels_model_guide)
    }

    suspend fun loadGeneralLabels(): Array<String> {
        return LabelsUtils.loadLabels(R.raw.labels_model_general)
    }
}
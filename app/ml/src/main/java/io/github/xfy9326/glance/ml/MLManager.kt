package io.github.xfy9326.glance.ml

import android.graphics.Bitmap
import io.github.xfy9326.atools.io.IOManager
import io.github.xfy9326.glance.ml.beans.DetectObject
import io.github.xfy9326.glance.ml.beans.PixelsData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object MLManager {
    private const val DEFAULT_CONF_THRESHOLD = 0.25f
    private const val DEFAULT_IOU_THRESHOLD = 0.45f

    // yolov5n_fp16_opt
    private const val MODEL_GUIDE_BIN_PATH = "models/guide.bin"
    private const val MODEL_GUIDE_PARAM_BIN_PATH = "models/guide.param.bin"

    // yolov5s_fp16_opt
    private const val MODEL_GENERAL_BIN_PATH = "models/general.bin"
    private const val MODEL_GENERAL_PARAM_BIN_PATH = "models/general.param.bin"

    fun isGPUInstanceCreated(): Boolean =
        NativeInterface.isGPUInstanceCreated()

    fun hasGPUSupport(): Boolean =
        NativeInterface.hasGPUSupport()

    suspend fun createGPUInstance(): Boolean = withContext(Dispatchers.Default) {
        NativeInterface.createGPUInstance()
    }

    fun isGuideModelInitialized(): Boolean =
        NativeInterface.isGeneralModelInitialized()

    fun isGeneralModelInitialized(): Boolean =
        NativeInterface.isGeneralModelInitialized()

    suspend fun initGuideModel(): Boolean = withContext(Dispatchers.IO) {
        NativeInterface.initGuideModel(IOManager.assetManager, MODEL_GUIDE_BIN_PATH, MODEL_GUIDE_PARAM_BIN_PATH)
    }

    suspend fun initGeneralModel(): Boolean = withContext(Dispatchers.IO) {
        NativeInterface.initGuideModel(IOManager.assetManager, MODEL_GENERAL_BIN_PATH, MODEL_GENERAL_PARAM_BIN_PATH)
    }

    suspend fun detectGuideModel(pixelsData: PixelsData, enableGPU: Boolean): Array<DetectObject>? = withContext(Dispatchers.Default) {
        NativeInterface.detectGuideModel(pixelsData, enableGPU, DEFAULT_CONF_THRESHOLD, DEFAULT_IOU_THRESHOLD)
    }

    suspend fun detectGeneralModel(bitmap: Bitmap, enableGPU: Boolean): Array<DetectObject>? = withContext(Dispatchers.Default) {
        NativeInterface.detectGeneralModel(bitmap, enableGPU, DEFAULT_CONF_THRESHOLD, DEFAULT_IOU_THRESHOLD)
    }
}
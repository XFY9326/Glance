package io.github.xfy9326.glance.ml.model

import android.graphics.Bitmap
import io.github.xfy9326.atools.io.IOManager
import io.github.xfy9326.atools.io.file.rawResFile
import io.github.xfy9326.atools.io.okio.source
import io.github.xfy9326.atools.io.okio.useBuffer
import io.github.xfy9326.glance.ml.NativeInterface
import io.github.xfy9326.glance.ml.R
import io.github.xfy9326.glance.ml.beans.DetectObject
import io.github.xfy9326.glance.ml.beans.PixelsData
import io.github.xfy9326.glance.ml.model.base.AbstractDetectionModel
import org.json.JSONArray

internal object DetectionModel : AbstractDetectionModel() {
    private const val MODEL_DETECTION_BIN_PATH = "models/detection.bin"
    private const val MODEL_DETECTION_PARAM_BIN_PATH = "models/detection.param.bin"

    override fun onInitModel(): Boolean =
        NativeInterface.initDetectionModel(IOManager.assetManager, MODEL_DETECTION_BIN_PATH, MODEL_DETECTION_PARAM_BIN_PATH)

    override fun isInitialized(): Boolean =
        NativeInterface.isDetectionModelInitialized()

    override fun onDetectByBitmap(bitmap: Bitmap, confThreshold: Float, iouThreshold: Float): Array<DetectObject>? =
        NativeInterface.detectByBitmap(bitmap, confThreshold, iouThreshold)

    override fun onDetectByPixelsData(pixelsData: PixelsData, confThreshold: Float, iouThreshold: Float): Array<DetectObject>? =
        NativeInterface.detectByPixelsData(pixelsData, confThreshold, iouThreshold)

    override fun onLoadLabels(): Array<String> =
        rawResFile(R.raw.labels_model_detection).source().useBuffer {
            JSONArray(readUtf8()).let { json ->
                Array(json.length()) {
                    json.optString(it)
                }
            }
        }
}
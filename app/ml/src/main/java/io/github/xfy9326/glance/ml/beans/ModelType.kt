package io.github.xfy9326.glance.ml.beans

import androidx.annotation.RawRes
import io.github.xfy9326.glance.ml.MLConfig
import io.github.xfy9326.glance.ml.R

enum class ModelType(
    internal val nativeTypeInt: Int,
    internal val binPath: String,
    internal val paramBinPath: String,
    @RawRes internal val labelsResId: Int,
    internal val confThreshold: Float,
    internal val iouThreshold: Float
) {
    GUIDE_MODEL(
        0,
        MLConfig.MODEL_GUIDE_BIN_PATH,
        MLConfig.MODEL_GUIDE_PARAM_BIN_PATH,
        R.raw.labels_model_guide,
        MLConfig.DEFAULT_CONF_THRESHOLD,
        MLConfig.DEFAULT_IOU_THRESHOLD
    ),
    GENERAL_MODEL(
        1,
        MLConfig.MODEL_GENERAL_BIN_PATH,
        MLConfig.MODEL_GENERAL_PARAM_BIN_PATH,
        R.raw.labels_model_general,
        MLConfig.DEFAULT_CONF_THRESHOLD,
        MLConfig.DEFAULT_IOU_THRESHOLD
    );
}
package io.github.xfy9326.glance.ml

internal object MLConfig {
    const val DEFAULT_CONF_THRESHOLD = 0.25f
    const val DEFAULT_IOU_THRESHOLD = 0.45f

    // yolov5n_fp16_opt
    const val MODEL_GUIDE_BIN_PATH = "models/guide.bin"
    const val MODEL_GUIDE_PARAM_BIN_PATH = "models/guide.param.bin"

    // yolov5s_fp16_opt
    const val MODEL_GENERAL_BIN_PATH = "models/general.bin"
    const val MODEL_GENERAL_PARAM_BIN_PATH = "models/general.param.bin"
}
#ifndef GLANCE_YOLOV5_H
#define GLANCE_YOLOV5_H

#include <jni.h>
#include <memory>
#include <android/asset_manager_jni.h>
#include "net.h"
#include "data.h"
#include "model.h"

namespace YoloV5Executor {
    using namespace std;
    using namespace YoloV5Model;

    bool load_model(AAssetManager *mgr, const ModelInfo &modelInfo, ncnn::Net &net, const char *bin, const char *param_bin);

    shared_ptr<vector<shared_ptr<DetectObject>>> launch(
            const ncnn::Net &net, const ModelInfo &modelInfo, const PixelsData &pixelsData,
            const bool enable_gpu, const float conf_threshold, const float iou_threshold
    );

    shared_ptr<vector<shared_ptr<DetectObject>>> launch(
            const ncnn::Net &net, const ModelInfo &modelInfo, JNIEnv *env, jobject bitmap,
            const bool enable_gpu, const float conf_threshold, const float iou_threshold
    );
}

#endif //GLANCE_YOLOV5_H

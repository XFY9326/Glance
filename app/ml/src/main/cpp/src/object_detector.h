#ifndef GLANCE_OBJECT_DETECTOR_H
#define GLANCE_OBJECT_DETECTOR_H

#include <android/asset_manager_jni.h>
#include "data.h"

namespace ObjectDetector {
    using namespace std;

    void clear_model();

    bool is_model_initialized();

    bool init_model(AAssetManager *mgr, const char *bin, const char *param_bin);

    shared_ptr<ncnn::Mat> extract_features(const PixelsData &pixelsData);

    shared_ptr<YoloV5Output>
    detect(const PixelsData &pixelsData, const float conf_threshold, const float iou_threshold, const bool extract_features);

    shared_ptr<YoloV5Output>
    detect(JNIEnv *env, jobject bitmap, const float conf_threshold, const float iou_threshold, const bool extract_features);
}

#endif //GLANCE_OBJECT_DETECTOR_H

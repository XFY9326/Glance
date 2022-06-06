#ifndef GLANCE_OBJECT_DETECTOR_H
#define GLANCE_OBJECT_DETECTOR_H

#include <android/asset_manager_jni.h>
#include "net.h"
#include "yolov5.h"

namespace ObjectDetector {
    using namespace std;

    void clear_guide_model();

    void clear_general_model();

    bool is_guide_model_initialized();

    bool is_general_model_initialized();

    bool init_guide_model(AAssetManager *mgr, const char *bin, const char *param_bin);

    bool init_general_model(AAssetManager *mgr, const char *bin, const char *param_bin);

    shared_ptr<vector<shared_ptr<DetectObject>>>
    detect_guide_model(const PixelsData &pixelsData, const bool enable_gpu, const float conf_threshold, const float iou_threshold);

    shared_ptr<vector<shared_ptr<DetectObject>>>
    detect_general_model(JNIEnv *env, jobject bitmap, const bool enable_gpu, const float conf_threshold, const float iou_threshold);
}

#endif //GLANCE_OBJECT_DETECTOR_H

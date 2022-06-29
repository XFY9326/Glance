#ifndef GLANCE_OBJECT_DETECTOR_H
#define GLANCE_OBJECT_DETECTOR_H

#include <android/asset_manager_jni.h>
#include "net.h"
#include "yolov5.h"
#include "data.h"

namespace ObjectDetector {
    using namespace std;

    void clear_model();

    bool is_model_initialized();

    bool init_model(AAssetManager *mgr, const char *bin, const char *param_bin);

    shared_ptr<vector<shared_ptr<DetectObject>>>
    detect(const PixelsData &pixelsData, const float conf_threshold, const float iou_threshold);

    shared_ptr<vector<shared_ptr<DetectObject>>>
    detect(JNIEnv *env, jobject bitmap, const float conf_threshold, const float iou_threshold);
}

#endif //GLANCE_OBJECT_DETECTOR_H

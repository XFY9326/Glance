#ifndef GLANCE_ML_MANAGER_H
#define GLANCE_ML_MANAGER_H

#include <android/asset_manager_jni.h>
#include <memory>
#include <vector>
#include "data.h"

namespace MLManager {
    using namespace std;

    void clear_models();

    bool is_models_initialized();

    bool init_models(AAssetManager *mgr);

    shared_ptr<MLOutput> analyze_image(
            const PixelsData &pixelsData,
            const bool request_caption,
            const float conf_threshold,
            const float iou_threshold
    );

    shared_ptr<MLOutput> analyze_image(
            JNIEnv *env, jobject bitmap,
            const bool request_caption,
            const float conf_threshold,
            const float iou_threshold
    );
}

#endif //GLANCE_ML_MANAGER_H

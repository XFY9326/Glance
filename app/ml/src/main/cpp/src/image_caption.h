#ifndef GLANCE_IMAGE_CAPTION_H
#define GLANCE_IMAGE_CAPTION_H

#include <android/asset_manager_jni.h>
#include <vector>
#include "mat.h"

namespace ImageCaption {
    using namespace std;

    void clear_model();

    bool is_model_initialized();

    bool init_model(
            AAssetManager *mgr,
            const char *features_bin, const char *features_param_bin,
            const char *embed_bin, const char *embed_param_bin,
            const char *gru_bin, const char *gru_param_bin
    );

    shared_ptr<vector<unsigned int>> generate(const ncnn::Mat &features);
}

#endif //GLANCE_IMAGE_CAPTION_H

#ifndef GLANCE_GRU_H
#define GLANCE_GRU_H

#include <memory>
#include <android/asset_manager_jni.h>
#include "net.h"
#include "model.h"

namespace GRUExecutor {
    using namespace std;
    using namespace GRUModel;

    bool load_features_model(AAssetManager *mgr, const ModelInfo &modelInfo, ncnn::Net &net, const char *bin, const char *param_bin);

    bool load_embed_model(AAssetManager *mgr, const ModelInfo &modelInfo, ncnn::Net &net, const char *bin, const char *param_bin);

    bool load_gru_model(AAssetManager *mgr, const ModelInfo &modelInfo, ncnn::Net &net, const char *bin, const char *param_bin);

    shared_ptr<vector<unsigned int>> launch(
            const ncnn::Net &features_net, const ncnn::Net &embed_net, const ncnn::Net &gru_net,
            const ncnn::Mat &features, const ModelInfo &modelInfo, const bool enable_gpu
    );
}

#endif //GLANCE_GRU_H

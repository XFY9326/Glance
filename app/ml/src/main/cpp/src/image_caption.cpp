#include "image_caption.h"
#include "gru.h"

#define GPU_SUPPORT true

namespace ImageCaption {
    using namespace std;

    static ncnn::Net features_net;
    static ncnn::Net embed_net;
    static ncnn::Net gru_net;
    static bool net_init = false;

    void clear_model() {
        features_net.clear();
        embed_net.clear();
        gru_net.clear();
        net_init = false;
    }

    bool is_model_initialized() {
        return net_init;
    }

    bool init_model(
            AAssetManager *mgr,
            const char *features_bin, const char *features_param_bin,
            const char *embed_bin, const char *embed_param_bin,
            const char *gru_bin, const char *gru_param_bin
    ) {
        if (!net_init) {
            net_init = GRUExecutor::load_features_model(mgr, GRUModel::gru, features_net, features_bin, features_param_bin);
            if (!net_init) return false;
            net_init = GRUExecutor::load_embed_model(mgr, GRUModel::gru, embed_net, embed_bin, embed_param_bin);
            if (!net_init) return false;
            net_init = GRUExecutor::load_gru_model(mgr, GRUModel::gru, gru_net, gru_bin, gru_param_bin);
            if (!net_init) return false;
        }
        return true;
    }

    shared_ptr<vector<unsigned int>> generate(const ncnn::Mat &features) {
        if (net_init) {
            return GRUExecutor::launch(
                    features_net, embed_net, gru_net,
                    features, GRUModel::gru,
                    GPU_SUPPORT
            );
        }
        return nullptr;
    }
}
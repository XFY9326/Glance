#include "ncnn_helper.h"
#include "log.h"
#include "utils.h"
#include "layer.h"

#define LOG_TAG "NCNNHelper"
#define ENABLE_GPU_COMPUTE true

namespace NCNNHelper {
    static bool gpu_instance_created = false;

    bool has_gpu_support() {
        return ncnn::get_gpu_count() > 0;
    }

    bool is_gpu_instance_created() {
        return has_gpu_support() && gpu_instance_created;
    }

    bool create_gpu_instance() {
        if (has_gpu_support()) {
            gpu_instance_created = ncnn::create_gpu_instance() == 0;
            return gpu_instance_created;
        }
        return false;
    }

    void destroy_gpu_instance() {
        if (gpu_instance_created) {
            ncnn::destroy_gpu_instance();
            gpu_instance_created = false;
        }
    }

    bool load_model(ncnn::Net &net, AAssetManager *mgr, const char *param_bin_path, const char *bin_path) {
        if (net.load_param_bin(mgr, param_bin_path) != 0) {
            LOG_E(LOG_TAG, "YoloV5Model param bin load failed: %s", param_bin_path);
            return false;
        }
        if (net.load_model(mgr, bin_path) != 0) {
            LOG_E(LOG_TAG, "YoloV5Model bin load failed: %s", bin_path);
            return false;
        }
        return true;
    }

    void configure_model(ncnn::Net &net) {
        ncnn::Option opt;
        opt.lightmode = true;
        opt.blob_allocator = &blob_pool_allocator;
        opt.workspace_allocator = &workspace_pool_allocator;
        opt.use_packing_layout = true;
        opt.use_vulkan_compute = ENABLE_GPU_COMPUTE && has_gpu_support();
        net.opt = opt;
    }
}
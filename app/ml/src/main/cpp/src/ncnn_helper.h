#ifndef GLANCE_NCNN_HELPER_H
#define GLANCE_NCNN_HELPER_H

#include <android/asset_manager_jni.h>
#include "net.h"

#define VULKAN_COMPUTE true
// Avoid unpacking fp16 when transferring mats between models
#define OUTPUT_MAT_TYPE 1


namespace NCNNHelper {
    bool is_gpu_instance_created();

    bool has_gpu_support();

    bool create_gpu_instance();

    void clear_allocator();

    void destroy_gpu_instance();

    bool load_model(ncnn::Net &net, AAssetManager *mgr, const char *param_bin_path, const char *bin_path);

    void configure_model(ncnn::Net &net);

    void configure_extractor(const ncnn::Net &net, ncnn::Extractor &extractor, const bool enable_gpu);
}


#endif //GLANCE_NCNN_HELPER_H

#ifndef GLANCE_NCNN_HELPER_H
#define GLANCE_NCNN_HELPER_H

#include <android/asset_manager_jni.h>
#include "net.h"

namespace NCNNHelper {
    static ncnn::UnlockedPoolAllocator blob_pool_allocator;
    static ncnn::PoolAllocator workspace_pool_allocator;

    bool is_gpu_instance_created();

    bool has_gpu_support();

    bool create_gpu_instance();

    void destroy_gpu_instance();

    bool load_model(ncnn::Net &net, AAssetManager *mgr, const char *param_bin_path, const char *bin_path);

    void configure_model(ncnn::Net &net);
}


#endif //GLANCE_NCNN_HELPER_H

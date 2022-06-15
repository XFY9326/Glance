#include "object_detector.h"
#include "yolov5.h"
#include "log.h"

#define LOG_TAG "ObjectDetector"

namespace ObjectDetector {
    using namespace std;

    static ncnn::Net net[MODEL_TYPE_SIZE];
    static bool net_init[MODEL_TYPE_SIZE] = {false};

    static const YoloV5Model::ModelInfo *get_model_info_by_type(const ModelType modelType) {
        switch (modelType) {
            case GUIDE_MODEL:
                return &YoloV5Model::yolov5_guide;
            case GENERAL_MODEL:
                return &YoloV5Model::yolov5_general;
        }
        return nullptr;
    }

    void clear_models() {
        for (auto &item: net) {
            item.clear();
        }
        memset(net_init, 0, sizeof(net_init));
    }

    bool is_model_initialized(const ModelType modelType) {
        return net_init[modelType];
    }

    bool init_model(const ModelType modelType, AAssetManager *mgr, const char *bin, const char *param_bin) {
        if (!net_init[modelType]) {
            net_init[modelType] = YoloV5Executor::load_model(mgr, *get_model_info_by_type(modelType), net[modelType], bin, param_bin);
            return net_init[modelType];
        }
        return true;
    }

    shared_ptr<vector<shared_ptr<DetectObject>>>
    detect(const ModelType modelType, const PixelsData &pixelsData, const bool enable_gpu, const float conf_threshold, const float iou_threshold) {
        if (net_init[modelType]) {
            return YoloV5Executor::launch(net[modelType], *get_model_info_by_type(modelType), pixelsData, enable_gpu, conf_threshold, iou_threshold);
        }
        return nullptr;
    }

    shared_ptr<vector<shared_ptr<DetectObject>>>
    detect(const ModelType modelType, JNIEnv *env, jobject bitmap, const bool enable_gpu, const float conf_threshold, const float iou_threshold) {
        if (net_init[modelType]) {
            return YoloV5Executor::launch(net[modelType], *get_model_info_by_type(modelType), env, bitmap, enable_gpu, conf_threshold, iou_threshold);
        }
        return nullptr;
    }
}
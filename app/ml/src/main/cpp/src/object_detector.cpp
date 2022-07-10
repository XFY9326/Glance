#include "object_detector.h"
#include "yolov5.h"

#define GPU_SUPPORT true

namespace ObjectDetector {
    using namespace std;

    static ncnn::Net net;
    static bool net_init = false;

    void clear_model() {
        net.clear();
        net_init = false;
    }

    bool is_model_initialized() {
        return net_init;
    }

    bool init_model(AAssetManager *mgr, const char *bin, const char *param_bin) {
        if (!net_init) {
            net_init = YoloV5Executor::load_model(mgr, YoloV5Model::yolov5s_6_1, net, bin, param_bin);
            return net_init;
        }
        return true;
    }

    shared_ptr<ncnn::Mat> extract_features(const PixelsData &pixelsData) {
        if (net_init) {
            return YoloV5Executor::extract_features(
                    net, YoloV5Model::yolov5s_6_1,
                    pixelsData, GPU_SUPPORT
            );
        }
        return nullptr;
    }

    shared_ptr<YoloV5Output>
    detect(const PixelsData &pixelsData, const float conf_threshold, const float iou_threshold, const bool extract_features) {
        if (net_init) {
            return YoloV5Executor::detect(
                    net, YoloV5Model::yolov5s_6_1,
                    pixelsData, GPU_SUPPORT,
                    conf_threshold, iou_threshold,
                    extract_features
            );
        }
        return nullptr;
    }

    shared_ptr<YoloV5Output>
    detect(JNIEnv *env, jobject bitmap, const float conf_threshold, const float iou_threshold, const bool extract_features) {
        if (net_init) {
            return YoloV5Executor::detect(
                    net, YoloV5Model::yolov5s_6_1,
                    env, bitmap, GPU_SUPPORT,
                    conf_threshold, iou_threshold,
                    extract_features
            );
        }
        return nullptr;
    }
}
#include "object_detector.h"
#include "yolov5.h"
#include "log.h"

#define LOG_TAG "ObjectDetector"
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

    shared_ptr<vector<shared_ptr<DetectObject>>>
    detect(const PixelsData &pixelsData, const float conf_threshold, const float iou_threshold) {
        if (net_init) {
            return YoloV5Executor::launch(net, YoloV5Model::yolov5s_6_1, pixelsData, GPU_SUPPORT, conf_threshold, iou_threshold);
        }
        return nullptr;
    }

    shared_ptr<vector<shared_ptr<DetectObject>>>
    detect(JNIEnv *env, jobject bitmap, const float conf_threshold, const float iou_threshold) {
        if (net_init) {
            return YoloV5Executor::launch(net, YoloV5Model::yolov5s_6_1, env, bitmap, GPU_SUPPORT, conf_threshold, iou_threshold);
        }
        return nullptr;
    }
}
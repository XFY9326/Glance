#include "object_detector.h"
#include "yolov5.h"
#include "log.h"

#define LOG_TAG "ObjectDetector"

namespace ObjectDetector {
    using namespace std;

    static ncnn::Net yolov5_guide_net;
    static ncnn::Net yolov5_general_net;

    static bool yolov5_guide_net_init = false;
    static bool yolov5_general_net_init = false;

    void clear_guide_model() {
        yolov5_guide_net.clear();
    }

    void clear_general_model() {
        yolov5_general_net.clear();
    }

    bool is_guide_model_initialized() {
        return yolov5_guide_net_init;
    }

    bool is_general_model_initialized() {
        return yolov5_general_net_init;
    }

    bool init_guide_model(AAssetManager *mgr, const char *bin, const char *param_bin) {
        if (!yolov5_guide_net_init) {
            yolov5_guide_net_init = YoloV5Executor::load_model(mgr, YoloV5Model::yolov5_guide, yolov5_guide_net, bin, param_bin);
            return yolov5_guide_net_init;
        }
        return true;
    }

    bool init_general_model(AAssetManager *mgr, const char *bin, const char *param_bin) {
        if (!yolov5_general_net_init) {
            yolov5_general_net_init = YoloV5Executor::load_model(mgr, YoloV5Model::yolov5_general, yolov5_general_net, bin, param_bin);
            return yolov5_general_net_init;
        }
        return true;
    }

    shared_ptr<vector<shared_ptr<DetectObject>>>
    detect_guide_model(const PixelsData &pixelsData, const bool enable_gpu, const float conf_threshold, const float iou_threshold) {
        if (yolov5_guide_net_init) {
            return YoloV5Executor::launch(yolov5_general_net, YoloV5Model::yolov5_general, pixelsData, enable_gpu, conf_threshold, iou_threshold);
        } else {
            LOG_E(LOG_TAG, "Object detector model 'yolov5_guide' hasn't initialized!");
            return nullptr;
        }
    }

    shared_ptr<vector<shared_ptr<DetectObject>>>
    detect_general_model(JNIEnv *env, jobject bitmap, const bool enable_gpu, const float conf_threshold, const float iou_threshold) {
        if (yolov5_general_net_init) {
            return YoloV5Executor::launch(yolov5_general_net, YoloV5Model::yolov5_general, env, bitmap, enable_gpu, conf_threshold, iou_threshold);
        } else {
            LOG_E(LOG_TAG, "Object detector model 'yolov5_general' hasn't initialized!");
            return nullptr;
        }
    }
}
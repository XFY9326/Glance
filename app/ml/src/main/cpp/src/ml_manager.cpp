#include "ml_manager.h"
#include "object_detector.h"
#include "image_caption.h"
#include "utils.h"
#include "log.h"

#define LOG_TAG "ML_MANAGER"

#define MODEL_DETECTION_BIN "models/detection.bin"
#define MODEL_DETECTION_PARAM_BIN "models/detection.param.bin"
#define MODEL_FEATURES_PREPROCESS_BIN "models/features_preprocess.bin"
#define MODEL_FEATURES_PREPROCESS_PARAM_BIN "models/features_preprocess.param.bin"
#define MODEL_EMBED_PREPROCESS_BIN "models/embed_preprocess.bin"
#define MODEL_EMBED_PREPROCESS_PARAM_BIN "models/embed_preprocess.param.bin"
#define MODEL_GRU_BIN "models/gru.bin"
#define MODEL_GRU_PARAM_BIN "models/gru.param.bin"

namespace MLManager {
    using namespace std;

    void clear_models() {
        ObjectDetector::clear_model();
        ImageCaption::clear_model();
    }

    bool is_models_initialized() {
        bool result = ObjectDetector::is_model_initialized();
        result = ImageCaption::is_model_initialized() && result;
        return result;
    }

    bool init_models(AAssetManager *mgr) {
        bool result = ObjectDetector::init_model(mgr, MODEL_DETECTION_BIN, MODEL_DETECTION_PARAM_BIN);
        result = ImageCaption::init_model(
                mgr,
                MODEL_FEATURES_PREPROCESS_BIN, MODEL_FEATURES_PREPROCESS_PARAM_BIN,
                MODEL_EMBED_PREPROCESS_BIN, MODEL_EMBED_PREPROCESS_PARAM_BIN,
                MODEL_GRU_BIN, MODEL_GRU_PARAM_BIN
        ) && result;
        return result;
    }

    shared_ptr<vector<int>> analyze_image_caption(const PixelsData &pixelsData) {
        auto features = ObjectDetector::extract_features(pixelsData);
        if (features != nullptr) {
            return ImageCaption::generate_captions(*features);
        } else {
            return nullptr;
        }
    }

    static shared_ptr<MLDetectOutput> internal_analyze_image(
            const YoloV5Output &detect_result,
            const bool request_caption
    ) {
        auto result = make_shared<MLDetectOutput>();
        result->objects = make_shared<vector<shared_ptr<DetectObject>>>(detect_result.objects);
        Utils::detected_object_descend_confidence_sort(*result->objects);
        if (request_caption) {
            if (detect_result.features != nullptr) {
                auto captions = ImageCaption::generate_captions(*detect_result.features);
                result->caption_ids = captions;
            } else {
                LOG_E(LOG_TAG, "Request captions but no features output!");
            }
        }
        return result;
    }

    shared_ptr<MLDetectOutput> analyze_image(
            const PixelsData &pixelsData,
            const bool request_caption,
            const float conf_threshold,
            const float iou_threshold
    ) {
        auto detect_result = ObjectDetector::detect(pixelsData, conf_threshold, iou_threshold, request_caption);
        return internal_analyze_image(*detect_result, request_caption);
    }

    shared_ptr<MLDetectOutput> analyze_image(
            JNIEnv *env, jobject bitmap,
            const bool request_caption,
            const float conf_threshold,
            const float iou_threshold
    ) {
        auto detect_result = ObjectDetector::detect(env, bitmap, conf_threshold, iou_threshold, request_caption);
        return internal_analyze_image(*detect_result, request_caption);
    }
}
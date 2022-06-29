#include "yolov5.h"
#include "utils.h"
#include "ncnn_helper.h"

#define YOLOV5_OUTPUT_META_SIZE 5
#define YOLOV5_INPUT_BORDER_PADDING 114.f

namespace YoloV5Executor {
    using namespace std;
    using namespace YoloV5Model;

    bool load_model(AAssetManager *mgr, const ModelInfo &modelInfo, ncnn::Net &net, const char *bin, const char *param_bin) {
        NCNNHelper::configure_model(net);
        bool load_success = NCNNHelper::load_model(net, mgr, param_bin, bin);
        if (load_success) {
            // Warm up
            ncnn::Mat input((int) modelInfo.input_width, (int) modelInfo.input_height, RGB_CHANNELS);
            ncnn::Extractor extractor = net.create_extractor();
            extractor.input(modelInfo.input_blob, input);
            ncnn::Mat output;
            for (auto &&item: modelInfo.output_layers) {
                extractor.extract(item.blob, output);
            }
        }
        return load_success;
    }

    static void get_resize_info(ResizeInfo &resize_info, const int width, const int height, const int target_width, const int target_height) {
        resize_info.ratio = (float) min(1.0 * target_width / width, 1.0 * target_height / height);
        resize_info.scaled_width = min((int) ((float) width * resize_info.ratio), (int) target_width);
        resize_info.scaled_height = min((int) ((float) height * resize_info.ratio), (int) target_height);
        resize_info.width_offset = (int) ((1.0 * target_width - resize_info.scaled_width) / 2.0);
        resize_info.height_offset = (int) ((1.0 * target_height - resize_info.scaled_height) / 2.0);
    }

    static void pre_process_input(ncnn::Mat &input, ncnn::Mat &output, const ResizeInfo &resize_info) {
        ncnn::copy_make_border(
                input, output,
                resize_info.height_offset, resize_info.height_offset,
                resize_info.width_offset, resize_info.width_offset,
                ncnn::BORDER_CONSTANT, YOLOV5_INPUT_BORDER_PADDING
        );
        output.substract_mean_normalize(YoloV5Model::means, YoloV5Model::norms);
    }

    static void pre_process_bitmap(JNIEnv *env, const jobject &bitmap, const ModelInfo &modelInfo, ncnn::Mat &mat, ResizeInfo &resize_info) {
        AndroidBitmapInfo bitmapInfo;
        AndroidBitmap_getInfo(env, bitmap, &bitmapInfo);
        get_resize_info(resize_info, (int) bitmapInfo.width, (int) bitmapInfo.height, (int) modelInfo.input_width, (int) modelInfo.input_height);
        ncnn::Mat input = ncnn::Mat::from_android_bitmap_resize(env, bitmap, ncnn::Mat::PIXEL_RGBA2RGB, resize_info.scaled_width, resize_info.scaled_height);
        pre_process_input(input, mat, resize_info);
    }

    static void pre_process_pixels(const PixelsData &pixelsData, const ModelInfo &modelInfo, ncnn::Mat &mat, ResizeInfo &resize_info) {
        get_resize_info(resize_info, pixelsData.width, pixelsData.height, (int) modelInfo.input_width, (int) modelInfo.input_height);
        ncnn::Mat input = ncnn::Mat::from_pixels_resize(
                pixelsData.pixels, ncnn::Mat::PIXEL_RGBA2RGB,
                pixelsData.width, pixelsData.height, pixelsData.stride,
                resize_info.scaled_width, resize_info.scaled_height);
        pre_process_input(input, mat, resize_info);
    }

    static void decode_proposals(
            const OutputLayer &layer, const unsigned int input_width, const unsigned int input_height, const ncnn::Mat &output,
            const float conf_threshold, map<int, unique_ptr<vector<shared_ptr<DetectObject>>>> &result) {
        const int num_grid = output.h;
        unsigned int num_grid_x;
        unsigned int num_grid_y;
        if (input_width > input_height) {
            num_grid_x = input_width / layer.stride;
            num_grid_y = num_grid / num_grid_x;
        } else {
            num_grid_y = input_width / layer.stride;
            num_grid_x = num_grid / num_grid_y;
        }
        const float reverse_conf_threshold = Utils::reverse_sigmoid(conf_threshold);
        for (int c = 0; c < YOLOV5_ANCHOR_SIZE; ++c) {
            const ncnn::Mat channel_feature = output.channel(c);

            for (int shift_y = 0; shift_y < num_grid_y; ++shift_y) {
                for (int shift_x = 0; shift_x < num_grid_x; ++shift_x) {
                    const float *feature = channel_feature.row(shift_x + shift_y * (int) num_grid_y);
                    if (feature[4] > reverse_conf_threshold) {
                        int max_conf_class_index = YOLOV5_OUTPUT_META_SIZE;
                        for (int i = max_conf_class_index + 1; i < output.w; ++i) {
                            if (feature[i] > feature[max_conf_class_index]) max_conf_class_index = i;
                        }
                        const float confidence = Utils::sigmoid(feature[max_conf_class_index]) * Utils::sigmoid(feature[4]);
                        if (confidence > conf_threshold) {
                            const double cx = (Utils::sigmoid(feature[0]) * 2.f - 0.5f + (float) shift_x) * (float) layer.stride;
                            const double cy = (Utils::sigmoid(feature[1]) * 2.f - 0.5f + (float) shift_y) * (float) layer.stride;
                            const double w = pow(Utils::sigmoid(feature[2]) * 2.f, 2) * layer.anchors[c][0];
                            const double h = pow(Utils::sigmoid(feature[3]) * 2.f, 2) * layer.anchors[c][1];
                            const int object_class_id = max_conf_class_index - YOLOV5_OUTPUT_META_SIZE;
                            auto new_object = make_shared<DetectObject>(
                                    max(0.0, cx - w / 2),
                                    max(0.0, cy - h / 2),
                                    min((double) input_width, cx + w / 2),
                                    min((double) input_height, cy + h / 2),
                                    object_class_id,
                                    confidence
                            );
                            if (result.find(object_class_id) == result.end()) {
                                result.emplace(object_class_id, make_unique<vector<shared_ptr<DetectObject>>>());
                            }
                            result[object_class_id]->emplace_back(new_object);
                        }
                    }
                }
            }
        }
    }

    static shared_ptr<vector<shared_ptr<DetectObject>>>
    process(const ncnn::Net &net, const ModelInfo &modelInfo, const ncnn::Mat &input, const bool enable_gpu, const float conf_threshold, const float iou_threshold) {
        ncnn::Extractor extractor = net.create_extractor();
        extractor.set_vulkan_compute(enable_gpu && net.opt.use_vulkan_compute && NCNNHelper::is_gpu_instance_created());
        extractor.input(modelInfo.input_blob, input);

        ncnn::Mat output;
        map<int, unique_ptr<vector<shared_ptr<DetectObject>>>> result;
        for (auto &&item: modelInfo.output_layers) {
            extractor.extract(item.blob, output);
            decode_proposals(item, input.w, input.h, output, conf_threshold, result);
        }
        return Utils::non_maximum_suppression(result, iou_threshold);
    }

    static void post_process(const shared_ptr<vector<shared_ptr<DetectObject>>> &objects, const ResizeInfo &resize_info) {
        for (auto &&object: *objects) {
            object->left = (float) (((double) object->left - resize_info.width_offset) / resize_info.ratio);
            object->top = (float) (((double) object->top - resize_info.height_offset) / resize_info.ratio);
            object->right = (float) (((double) object->right - resize_info.width_offset) / resize_info.ratio);
            object->bottom = (float) (((double) object->bottom - resize_info.height_offset) / resize_info.ratio);
        }
    }

    static shared_ptr<vector<shared_ptr<DetectObject>>> run_model(
            const ncnn::Net &net, const ModelInfo &modelInfo, const ncnn::Mat &input, const ResizeInfo &resize_info,
            const bool enable_gpu, const float conf_threshold, const float iou_threshold
    ) {
        auto output = process(net, modelInfo, input, enable_gpu, conf_threshold, iou_threshold);
        post_process(output, resize_info);
        return output;
    }

    // Require RGBA_8888
    shared_ptr<vector<shared_ptr<DetectObject>>> launch(
            const ncnn::Net &net, const ModelInfo &modelInfo, const PixelsData &pixelsData,
            const bool enable_gpu, const float conf_threshold, const float iou_threshold
    ) {
        ncnn::Mat input;
        ResizeInfo resize_info;
        pre_process_pixels(pixelsData, modelInfo, input, resize_info);
        return run_model(net, modelInfo, input, resize_info, enable_gpu, conf_threshold, iou_threshold);
    }

    // Require RGBA_8888
    shared_ptr<vector<shared_ptr<DetectObject>>> launch(
            const ncnn::Net &net, const ModelInfo &modelInfo, JNIEnv *env, jobject bitmap,
            const bool enable_gpu, const float conf_threshold, const float iou_threshold
    ) {
        ncnn::Mat input;
        ResizeInfo resize_info;
        pre_process_bitmap(env, bitmap, modelInfo, input, resize_info);
        return run_model(net, modelInfo, input, resize_info, enable_gpu, conf_threshold, iou_threshold);
    }
}
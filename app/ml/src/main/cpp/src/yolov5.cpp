#include "yolov5.h"
#include "utils.h"
#include "ncnn_helper.h"

#define YOLOV5_OUTPUT_DX 0
#define YOLOV5_OUTPUT_DY 1
#define YOLOV5_OUTPUT_DW 2
#define YOLOV5_OUTPUT_DH 3
#define YOLOV5_OUTPUT_PB 4
#define YOLOV5_OUTPUT_META_SIZE 5
#define YOLOV5_INPUT_BORDER_PADDING 114.f

// Avoid unpacking fp16 when transferring mats between models
#define OUTPUT_MAT_TYPE 1

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
            extractor.extract(modelInfo.output_features_blob, output);
            for (auto &&item: modelInfo.output_layers) {
                extractor.extract(item.blob, output);
            }
            extractor.clear();
        }
        return load_success;
    }

    static void get_resize_info(ResizeInfo &resize_info, const int width, const int height, const int target_width, const int target_height, const int max_stride) {
        if (width > height) {
            resize_info.ratio = (float) ((double) target_width / width);
            resize_info.scaled_width = target_width;
            resize_info.scaled_height = (int) ((double) height * resize_info.ratio);
        } else {
            resize_info.ratio = (float) ((double) target_height / height);
            resize_info.scaled_width = (int) ((double) width * resize_info.ratio);
            resize_info.scaled_height = target_height;
        }
        resize_info.width_offset = ((resize_info.scaled_width + max_stride - 1) / max_stride * max_stride - resize_info.scaled_width) / 2;
        resize_info.height_offset = ((resize_info.scaled_height + max_stride - 1) / max_stride * max_stride - resize_info.scaled_height) / 2;
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
        get_resize_info(resize_info, (int) bitmapInfo.width, (int) bitmapInfo.height, (int) modelInfo.input_width, (int) modelInfo.input_height, (int) modelInfo.max_stride);
        ncnn::Mat input = ncnn::Mat::from_android_bitmap_resize(env, bitmap, ncnn::Mat::PIXEL_RGBA2RGB, resize_info.scaled_width, resize_info.scaled_height);
        pre_process_input(input, mat, resize_info);
    }

    static void pre_process_pixels(const PixelsData &pixelsData, const ModelInfo &modelInfo, ncnn::Mat &mat, ResizeInfo &resize_info) {
        get_resize_info(resize_info, pixelsData.width, pixelsData.height, (int) modelInfo.input_width, (int) modelInfo.input_height, (int) modelInfo.max_stride);
        ncnn::Mat input = ncnn::Mat::from_pixels_resize(
                pixelsData.pixels, ncnn::Mat::PIXEL_RGBA2RGB,
                pixelsData.width, pixelsData.height, pixelsData.stride,
                resize_info.scaled_width, resize_info.scaled_height);
        pre_process_input(input, mat, resize_info);
    }

    static void decode_proposals(
            const OutputLayer &layer, const unsigned int input_width, const unsigned int input_height, const ncnn::Mat &output,
            const float conf_threshold, vector<shared_ptr<DetectObject>> &result) {
        const int feature_size = output.w;
        const int anchor_size = output.c;
        const int num_x = (int) input_width / layer.stride;
        const int num_y = (int) input_height / layer.stride;

        const double reverse_conf_threshold = Utils::reverse_sigmoid(conf_threshold);
        for (int a = 0; a < anchor_size; ++a) {
            const ncnn::Mat anchor_features = output.channel(a);
            for (int y = 0; y < num_y; ++y) {
                for (int x = 0; x < num_x; ++x) {
                    const float *features = anchor_features.row(x + y * num_x);
                    if (features[YOLOV5_OUTPUT_PB] > reverse_conf_threshold) {
                        int class_index = YOLOV5_OUTPUT_META_SIZE;
                        for (int i = YOLOV5_OUTPUT_META_SIZE + 1; i < feature_size; ++i) {
                            if (features[i] > features[class_index]) class_index = i;
                        }

                        auto confidence = (float) (Utils::sigmoid(features[class_index]) * Utils::sigmoid(features[YOLOV5_OUTPUT_PB]));
                        if (confidence > conf_threshold) {
                            auto pb_cx = (float) ((Utils::sigmoid(features[YOLOV5_OUTPUT_DX]) * 2 - 0.5 + x) * layer.stride);
                            auto pb_cy = (float) ((Utils::sigmoid(features[YOLOV5_OUTPUT_DY]) * 2 - 0.5 + y) * layer.stride);
                            auto pb_w = (float) (pow(Utils::sigmoid(features[YOLOV5_OUTPUT_DW]) * 2, 2) * layer.anchors[a][0]);
                            auto pb_h = (float) (pow(Utils::sigmoid(features[YOLOV5_OUTPUT_DH]) * 2, 2) * layer.anchors[a][1]);
                            int class_id = class_index - YOLOV5_OUTPUT_META_SIZE;

                            auto new_object = make_shared<DetectObject>(
                                    max(0.0f, pb_cx - pb_w * 0.5f),
                                    max(0.0f, pb_cy - pb_h * 0.5f),
                                    min((float) input_width, pb_cx + pb_w * 0.5f),
                                    min((float) input_height, pb_cy + pb_h * 0.5f),
                                    confidence,
                                    class_id
                            );

                            result.emplace_back(new_object);
                        }
                    }
                }
            }
        }
    }

    static shared_ptr<YoloV5Output>
    process(const ncnn::Net &net, const ModelInfo &modelInfo, const ncnn::Mat &input, const bool enable_gpu, const float conf_threshold, const float iou_threshold) {
        ncnn::Extractor extractor = net.create_extractor();
        if (net.opt.use_vulkan_compute) {
            extractor.set_vulkan_compute(enable_gpu && NCNNHelper::is_gpu_instance_created());
        }
        extractor.input(modelInfo.input_blob, input);

        auto yolov5_output = make_shared<YoloV5Output>();

        extractor.extract(modelInfo.output_features_blob, yolov5_output->features, OUTPUT_MAT_TYPE);

        ncnn::Mat output;
        vector<shared_ptr<DetectObject>> result;

        for (auto &&item: modelInfo.output_layers) {
            extractor.extract(item.blob, output);
            decode_proposals(item, input.w, input.h, output, conf_threshold, result);
        }
        Utils::non_maximum_suppression(result, iou_threshold, yolov5_output->objects);
        return yolov5_output;
    }

    static void post_process(const vector<shared_ptr<DetectObject>> &objects, const ResizeInfo &resize_info) {
        for (auto &&object: objects) {
            object->left = (float) (((double) object->left - resize_info.width_offset) / resize_info.ratio);
            object->top = (float) (((double) object->top - resize_info.height_offset) / resize_info.ratio);
            object->right = (float) (((double) object->right - resize_info.width_offset) / resize_info.ratio);
            object->bottom = (float) (((double) object->bottom - resize_info.height_offset) / resize_info.ratio);
        }
    }

    static shared_ptr<YoloV5Output> run_model(
            const ncnn::Net &net, const ModelInfo &modelInfo, const ncnn::Mat &input, const ResizeInfo &resize_info,
            const bool enable_gpu, const float conf_threshold, const float iou_threshold
    ) {
        auto output = process(net, modelInfo, input, enable_gpu, conf_threshold, iou_threshold);
        post_process(output->objects, resize_info);
        return output;
    }

    // Require RGBA_8888
    shared_ptr<YoloV5Output> launch(
            const ncnn::Net &net, const ModelInfo &modelInfo, const PixelsData &pixelsData,
            const bool enable_gpu, const float conf_threshold, const float iou_threshold
    ) {
        ncnn::Mat input;
        ResizeInfo resize_info;
        pre_process_pixels(pixelsData, modelInfo, input, resize_info);
        return run_model(net, modelInfo, input, resize_info, enable_gpu, conf_threshold, iou_threshold);
    }

    // Require RGBA_8888
    shared_ptr<YoloV5Output> launch(
            const ncnn::Net &net, const ModelInfo &modelInfo, JNIEnv *env, jobject bitmap,
            const bool enable_gpu, const float conf_threshold, const float iou_threshold
    ) {
        ncnn::Mat input;
        ResizeInfo resize_info;
        pre_process_bitmap(env, bitmap, modelInfo, input, resize_info);
        return run_model(net, modelInfo, input, resize_info, enable_gpu, conf_threshold, iou_threshold);
    }
}
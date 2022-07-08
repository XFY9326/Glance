#include "gru.h"
#include "ncnn_helper.h"
#include "utils.h"

// Avoid unpacking fp16 when transferring mats between models
#define OUTPUT_MAT_TYPE 1

namespace GRUExecutor {
    using namespace std;
    using namespace GRUModel;

    bool load_features_model(AAssetManager *mgr, const ModelInfo &modelInfo, ncnn::Net &net, const char *bin, const char *param_bin) {
        NCNNHelper::configure_model(net);
        bool load_success = NCNNHelper::load_model(net, mgr, param_bin, bin);
        if (load_success) {
            // Warm up
            ncnn::Extractor extractor = net.create_extractor();

            ncnn::Mat input((int) modelInfo.hidden_size);
            extractor.input(modelInfo.features_input_blob, input);

            ncnn::Mat output;
            extractor.extract(modelInfo.features_output_blob, output);

            extractor.clear();
        }
        return load_success;
    }

    bool load_embed_model(AAssetManager *mgr, const ModelInfo &modelInfo, ncnn::Net &net, const char *bin, const char *param_bin) {
        NCNNHelper::configure_model(net);
        bool load_success = NCNNHelper::load_model(net, mgr, param_bin, bin);
        if (load_success) {
            // Warm up
            ncnn::Extractor extractor = net.create_extractor();

            ncnn::Mat input(1);
            extractor.input(modelInfo.embed_input_blob, input);

            ncnn::Mat output;
            extractor.extract(modelInfo.embed_output_blob, output);

            extractor.clear();
        }
        return load_success;
    }

    bool load_gru_model(AAssetManager *mgr, const ModelInfo &modelInfo, ncnn::Net &net, const char *bin, const char *param_bin) {
        NCNNHelper::configure_model(net);
        bool load_success = NCNNHelper::load_model(net, mgr, param_bin, bin);
        if (load_success) {
            // Warm up
            ncnn::Extractor extractor = net.create_extractor();

            ncnn::Mat input_predict((int) modelInfo.embed_size, 1);
            ncnn::Mat input_states((int) modelInfo.hidden_size, 1, 1);
            extractor.input(modelInfo.gru_input_blob_predict, input_predict);
            extractor.input(modelInfo.gru_input_blob_states, input_states);

            ncnn::Mat output_predict;
            ncnn::Mat output_states;
            extractor.extract(modelInfo.gru_output_blob_predict, output_predict);
            extractor.extract(modelInfo.gru_output_blob_states, output_states);

            extractor.clear();
        }
        return load_success;
    }

    static void run_features_model(
            const ncnn::Net &net, const ModelInfo &modelInfo,
            const ncnn::Mat &features, ncnn::Mat &output, const bool enable_gpu
    ) {
        ncnn::Extractor extractor = net.create_extractor();
        if (net.opt.use_vulkan_compute) {
            extractor.set_vulkan_compute(enable_gpu && NCNNHelper::is_gpu_instance_created());
        }

        extractor.input(modelInfo.features_input_blob, features);
        extractor.extract(modelInfo.features_output_blob, output, OUTPUT_MAT_TYPE);
    }

    static void run_embed_model(
            const ncnn::Net &net, const ModelInfo &modelInfo,
            const ncnn::Mat &word_features, ncnn::Mat &output, const bool enable_gpu
    ) {
        ncnn::Extractor extractor = net.create_extractor();
        if (net.opt.use_vulkan_compute) {
            extractor.set_vulkan_compute(enable_gpu && NCNNHelper::is_gpu_instance_created());
        }

        extractor.input(modelInfo.embed_input_blob, word_features);
        extractor.extract(modelInfo.embed_output_blob, output, OUTPUT_MAT_TYPE);
    }

    static void run_gru_model(
            const ncnn::Net &net, const ModelInfo &modelInfo,
            const ncnn::Mat &predict, ncnn::Mat &states,
            ncnn::Mat &output_predict, const bool enable_gpu
    ) {
        ncnn::Extractor extractor = net.create_extractor();
        if (net.opt.use_vulkan_compute) {
            extractor.set_vulkan_compute(enable_gpu && NCNNHelper::is_gpu_instance_created());
        }

        extractor.input(modelInfo.gru_input_blob_predict, predict);
        extractor.input(modelInfo.gru_input_blob_states, states);

        extractor.extract(modelInfo.gru_output_blob_states, states, OUTPUT_MAT_TYPE);
        extractor.extract(modelInfo.gru_output_blob_predict, output_predict);
    }

    shared_ptr<vector<int>> launch(
            const ncnn::Net &features_net, const ncnn::Net &embed_net, const ncnn::Net &gru_net,
            const ncnn::Mat &features, const ModelInfo &modelInfo, const bool enable_gpu
    ) {
        auto result = make_shared<vector<int>>();
        bool is_first_recurrent = true;
        int predict_word = 0;
        ncnn::Mat last_predict;
        ncnn::Mat output_predict;
        ncnn::Mat word_features(1);
        ncnn::Mat states((int) modelInfo.hidden_size, 1, 1);
        states.fill(0);
        for (int i = 0; i < modelInfo.max_seq; ++i) {
            if (is_first_recurrent) {
                run_features_model(
                        features_net, modelInfo,
                        features, last_predict, enable_gpu
                );
                is_first_recurrent = false;
            } else {
                // ncnn::Mat treat all value as float
                // But embed input should be int in memory
                // Int 1 -> Memory 01 00
                word_features.fill(predict_word);
                run_embed_model(
                        embed_net, modelInfo,
                        word_features, last_predict, enable_gpu
                );
            }
            run_gru_model(
                    gru_net, modelInfo,
                    last_predict, states, output_predict, enable_gpu
            );
            predict_word = (int) Utils::arg_max_dim_1(output_predict);
            if (predict_word == modelInfo.stop_word_id) {
                break;
            } else {
                result->push_back(predict_word);
            }
        }
        return result;
    }
}
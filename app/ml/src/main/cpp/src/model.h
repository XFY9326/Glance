#ifndef GLANCE_MODEL_H
#define GLANCE_MODEL_H

#include "utils.h"

#define YOLOV5_MAX_STRIDE 32
#define YOLOV5_ANCHOR_SIZE 3
#define YOLOV5_OUTPUT_LAYER_SIZE 3

namespace YoloV5Model {

    struct OutputLayer {
        const int blob;
        const int stride;
        const int anchors[YOLOV5_ANCHOR_SIZE][2];
    };

    struct ModelInfo {
        const unsigned int input_width;
        const unsigned int input_height;
        const unsigned int max_stride;
        const int input_blob;
        const OutputLayer output_layers[YOLOV5_OUTPUT_LAYER_SIZE];
        const int output_features_blob;
    };

    static const float means[RGB_CHANNELS]{0.f, 0.f, 0.f};
    static const float norms[RGB_CHANNELS]{1.f / 255.f, 1.f / 255.f, 1.f / 255.f};

    // BLOB index comes from detection.id.h
    static const ModelInfo yolov5s_6_1{
            640, 640,
            YOLOV5_MAX_STRIDE,
            // BLOB_images
            0,
            {
                    // BLOB_output
                    {193, 8, {{10, 13}, {16, 30}, {33, 23}}},
                    // BLOB_353
                    {196, 16, {{30, 61}, {62, 45}, {59, 119}}},
                    // BLOB_367
                    {199, 32, {{116, 90}, {156, 198}, {373, 326}}}
            },
            // BLOB_features
            102
    };
}

#define GRU_MAX_SEQ 50
#define GRU_STOP_WORD_ID 2
#define GRU_EMBED_SIZE 256
#define GRU_HIDDEN_SIZE 512

namespace GRUModel {
    struct ModelInfo {
        const unsigned int max_seq;
        const unsigned int embed_size;
        const unsigned int hidden_size;
        const unsigned int stop_word_id;
        const int features_input_blob;
        const int features_output_blob;
        const int embed_input_blob;
        const int embed_output_blob;
        const int gru_input_blob_predict;
        const int gru_input_blob_states;
        const int gru_output_blob_predict;
        const int gru_output_blob_states;
    };

    static const ModelInfo gru{
            GRU_MAX_SEQ,
            GRU_EMBED_SIZE,
            GRU_HIDDEN_SIZE,
            GRU_STOP_WORD_ID,
            // features_preprocess.id.h BLOB_in0
            0,
            // features_preprocess.id.h BLOB_out0
            1,
            // embed_preprocess.id.h BLOB_in0
            0,
            // embed_preprocess.id.h BLOB_out0
            1,
            // gru.id.h BLOB_in0
            0,
            // gru.id.h BLOB_in1
            1,
            // gru.id.h BLOB_out0
            4,
            // gru.id.h BLOB_out1
            3
    };
}

#endif //GLANCE_MODEL_H

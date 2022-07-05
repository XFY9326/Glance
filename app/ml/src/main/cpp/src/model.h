#ifndef GLANCE_MODEL_H
#define GLANCE_MODEL_H

#include "utils.h"

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
        const int input_blob;
        const OutputLayer output_layers[YOLOV5_OUTPUT_LAYER_SIZE];
        const int output_features_blob;
    };

    static const float means[RGB_CHANNELS]{0.f, 0.f, 0.f};
    static const float norms[RGB_CHANNELS]{1.f / 255.f, 1.f / 255.f, 1.f / 255.f};

    // BLOB index comes from detection.id.h
    static const ModelInfo yolov5s_6_1{
            640, 640,
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

#endif //GLANCE_MODEL_H

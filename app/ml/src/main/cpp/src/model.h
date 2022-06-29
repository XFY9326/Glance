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
    };

    static const float means[RGB_CHANNELS]{0.f, 0.f, 0.f};
    static const float norms[RGB_CHANNELS]{1.f / 255.f, 1.f / 255.f, 1.f / 255.f};

    static const ModelInfo yolov5n_6_1{
            320, 320,
            // BLOB_images
            0,
            {
                    // BLOB_output
                    {190, 8, {{10, 13}, {16, 30}, {33, 23}}},
                    // BLOB_353
                    {193, 16, {{30, 61}, {62, 45}, {59, 119}}},
                    // BLOB_367
                    {196, 32, {{116, 90}, {156, 198}, {373, 326}}}
            }
    };
}

#endif //GLANCE_MODEL_H

#ifndef GLANCE_DATA_H
#define GLANCE_DATA_H

#include <vector>
#include <memory>
#include "mat.h"

struct DetectObject {
    float left;
    float top;
    float right;
    float bottom;
    float confidence;
    int class_id;

    DetectObject(float left, float top, float right, float bottom, float confidence, int class_id) :
            left(left), top(top), right(right), bottom(bottom), confidence(confidence), class_id(class_id) {}
};

struct YoloV5Output {
    std::vector<std::shared_ptr<DetectObject>> objects;
    std::shared_ptr<ncnn::Mat> features = nullptr;
};

struct MLDetectOutput {
    std::shared_ptr<std::vector<std::shared_ptr<DetectObject>>> objects = nullptr;
    std::shared_ptr<std::vector<int>> caption_ids = nullptr;
};

struct ResizeInfo {
    int width_offset = 0;
    int height_offset = 0;
    int scaled_width;
    int scaled_height;
    double ratio;

    ResizeInfo() {}
};

struct PixelsData {
    int width;
    int height;
    int stride;
    unsigned char *pixels;

    PixelsData() {}

    PixelsData(int width, int height, int stride, unsigned char *pixels) :
            width(width), height(height), stride(stride), pixels(pixels) {}
};

#endif //GLANCE_DATA_H

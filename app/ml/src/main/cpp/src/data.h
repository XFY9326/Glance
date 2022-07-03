#ifndef GLANCE_DATA_H
#define GLANCE_DATA_H

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

struct ResizeInfo {
    int width_offset;
    int height_offset;
    int scaled_width;
    int scaled_height;
    float ratio;

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

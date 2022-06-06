#ifndef GLANCE_DATA_H
#define GLANCE_DATA_H

struct DetectObject {
    float left;
    float top;
    float right;
    float bottom;
    int class_id;
    float confidence;

    DetectObject(float left, float top, float right, float bottom, int class_id, float confidence) :
            left(left), top(top), right(right), bottom(bottom), class_id(class_id), confidence(confidence) {}
};

struct ResizeInfo {
    int width_offset;
    int height_offset;
    int scaled_width;
    int scaled_height;
    float ratio;
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

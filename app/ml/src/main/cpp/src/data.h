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

#define MODEL_TYPE_SIZE 2

// Must be ordered number from 0
enum ModelType {
    GUIDE_MODEL = 0, GENERAL_MODEL = 1
};

#endif //GLANCE_DATA_H

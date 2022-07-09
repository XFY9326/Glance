#ifndef GLANCE_UTILS_H
#define GLANCE_UTILS_H

#include <map>
#include <vector>
#include <memory>
#include "data.h"

#define RGB_CHANNELS 3

namespace Utils {
    using namespace std;

    double sigmoid(double x);

    double reverse_sigmoid(double f);

    void detected_object_descend_confidence_sort(vector<shared_ptr<DetectObject>> &objects);

    void non_maximum_suppression(
            vector<shared_ptr<DetectObject>> &objects,
            const float iou_threshold,
            vector<shared_ptr<DetectObject>> &outputs
    );

    unsigned int arg_max_dim_1(ncnn::Mat &mat);
}

#endif //GLANCE_UTILS_H

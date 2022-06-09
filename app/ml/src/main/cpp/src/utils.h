#ifndef GLANCE_UTILS_H
#define GLANCE_UTILS_H

#include <map>
#include <vector>
#include <memory>
#include "data.h"

#define RGB_CHANNELS 3

namespace Utils {
    using namespace std;

    float sigmoid(float x);

    float reverse_sigmoid(float f);

    float intersection_over_union(const DetectObject &r1, const DetectObject &r2);

    shared_ptr<vector<shared_ptr<DetectObject>>>
    non_maximum_suppression(const map<int, unique_ptr<vector<shared_ptr<DetectObject>>>> &objects_map, const float iou_threshold);
}

#endif //GLANCE_UTILS_H

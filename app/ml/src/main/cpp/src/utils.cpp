#include "utils.h"

namespace Utils {
    using namespace std;

    double sigmoid(double x) {
        x = min(x, 88.3762626647949);
        x = max(x, -88.3762626647949);
        return 1.f / (1.f + exp(-x));
    }

    double reverse_sigmoid(double f) {
        return -1.0f * log((1.0f / (f + 1e-8)) - 1.0f);
    }

    static const auto detected_object_descend_sorter = [](const shared_ptr<DetectObject> &a, const shared_ptr<DetectObject> &b) {
        return a->confidence < b->confidence;
    };

    static float detect_object_area(const DetectObject &obj) {
        return (obj.right - obj.left) * (obj.bottom - obj.top);
    }

    static float detect_object_intersection_area(const DetectObject &o1, const DetectObject &o2) {
        const float inner_width = min(o1.right, o2.right) - max(o1.left, o2.left);
        const float inner_height = min(o1.bottom, o2.bottom) - max(o1.top, o2.top);
        return (inner_width < 0 || inner_height < 0) ? 0 : inner_width * inner_height;
    }

    void non_maximum_suppression(
            vector<shared_ptr<DetectObject>> &objects,
            const float iou_threshold,
            vector<shared_ptr<DetectObject>> &outputs
    ) {
        sort(objects.begin(), objects.end(), detected_object_descend_sorter);
        shared_ptr<DetectObject> o1, o2;
        while (!objects.empty()) {
            outputs.emplace_back(objects.back());
            objects.pop_back();
            auto it = objects.begin();
            while (it != objects.end()) {
                o1 = outputs.back();
                o2 = *it;
                float intersection_area = detect_object_intersection_area(*o1, *o2);
                if (intersection_area > 0) {
                    float iou = detect_object_area(*o1) + detect_object_area(*o2) - intersection_area;
                    if (iou < iou_threshold) {
                        ++it;
                    } else {
                        objects.erase(it);
                    }
                } else {
                    ++it;
                }
            }
        }
    }

    unsigned int arg_max_dim_1(ncnn::Mat &mat) {
        unsigned int index = 0;
        if (mat.dims == 1) {
            for (unsigned int i = 1; i < mat.w; ++i) {
                if (mat.channel(0).row(0)[i] > mat.channel(0).row(0)[index]) {
                    index = i;
                }
            }
        }
        return index;
    }
}
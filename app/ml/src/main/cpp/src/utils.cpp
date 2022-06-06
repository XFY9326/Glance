#include "utils.h"

namespace Utils {
    using namespace std;

    static const auto detected_object_comparator = [](const shared_ptr<DetectObject> &a, const shared_ptr<DetectObject> &b) {
        return a->confidence < b->confidence;
    };

    float intersection_over_union(const DetectObject &r1, const DetectObject &r2) {
        const double internal_width = min(r1.right, r2.right) - max(r1.left, r2.left);
        const double internal_height = min(r1.bottom, r2.bottom) - max(r1.top, r2.top);
        const double intersection_area = (internal_width < 0 || internal_height < 0) ? 0 : internal_width * internal_height;
        const double union_area = (r1.right - r1.left) * (r1.bottom - r1.top) + (r2.right - r2.left) * (r2.bottom - r2.top) - intersection_area;
        return (float) (intersection_area / union_area);
    }

    shared_ptr<vector<shared_ptr<DetectObject>>>
    non_maximum_suppression(const map<int, unique_ptr<vector<shared_ptr<DetectObject>>>> &objects_map, const float iou_threshold) {
        auto picked_objects = make_shared<vector<shared_ptr<DetectObject>>>();
        for (auto const &entry: objects_map) {
            sort(entry.second->begin(), entry.second->end(), detected_object_comparator);
            while (!entry.second->empty()) {
                picked_objects->emplace_back(entry.second->back());
                entry.second->pop_back();
                auto it = entry.second->begin();
                while (it != entry.second->end()) {
                    if (intersection_over_union(*(picked_objects->back()), *(*it)) >= iou_threshold) {
                        entry.second->erase(it);
                    } else {
                        ++it;
                    }
                }
            }
        }
        return picked_objects;
    }
}
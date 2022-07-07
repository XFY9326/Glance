#ifndef GLANCE_DEBUG_H
#define GLANCE_DEBUG_H

#include <vector>
#include <string>
#include <sstream>
#include "mat.h"
#include "log.h"

namespace Debug {
    static void mat_content_print(const char *name, const ncnn::Mat &m, const unsigned int limit) {
        long counter = 0;
        if (name != nullptr) {
            LOG_D("NCNN MAT", "Mat: %s", name);
        }
        for (int q = 0; q < m.c; q++) {
            for (int y = 0; y < m.h; y++) {
                for (int x = 0; x < m.w; x++) {
                    LOG_D("NCNN MAT", "%f ", m.channel(q).row(y)[x]);
                    if (++counter >= limit) {
                        LOG_D("NCNN MAT", "--------------------------------------");
                        return;
                    }
                }
            }
        }
        LOG_D("NCNN MAT", "--------------------------------------");
    }

    static void mat_content_print(const char *name, const ncnn::Mat &m) {
        return mat_content_print(name, m, 10);
    }

    static void mat_info_print(const char *name, const ncnn::Mat &m) {
        if (name != nullptr) {
            LOG_D("NCNN MAT", "Mat: %s", name);
        }
        LOG_D("NCNN MAT", "C %d H %d W %d   D %d", m.c, m.h, m.w, m.dims);
    }

    template<typename T>
    static void vector_print(const char *name, const std::vector <T> vec) {
        if (name != nullptr) {
            LOG_D("VECTOR", "Vector: %s", name);
        }
        std::stringstream text;
        text << "[";
        for (int i = 0; i < vec.size(); ++i) {
            text << vec[i];
            if (i != vec.size() - 1) {
                text << ", ";
            }
        }
        text << "]";
        LOG_D("VECTOR", "%s", text.str().c_str());
    }
}

#endif //GLANCE_DEBUG_H

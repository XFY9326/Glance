#ifndef GLANCE_JVM_CONVERT_H
#define GLANCE_JVM_CONVERT_H

#include <jni.h>
#include <memory>
#include <vector>
#include "data.h"

namespace JVMConvert {
    using namespace std;

    bool init(JNIEnv *env);

    jintArray caption_ids_vector_to_jvm(JNIEnv *env, const vector<int> &output);

    jobject ml_detect_output_to_jvm(JNIEnv *env, const MLDetectOutput &output);

    bool pixels_data_to_native(JNIEnv *env, jobject pixels_data, PixelsData &pixelsData);

    void clear(JNIEnv *env);
}

#endif //GLANCE_JVM_CONVERT_H

#ifndef GLANCE_JVM_CONVERT_H
#define GLANCE_JVM_CONVERT_H

#include <jni.h>
#include <memory>
#include <vector>
#include "data.h"

namespace JVMConvert {
    using namespace std;

    bool init(JNIEnv *env);

    jobjectArray output_vector_to_jvm(JNIEnv *env, const shared_ptr<vector<shared_ptr<DetectObject>>> &output);

    bool pixels_data_to_native(JNIEnv *env, jobject pixels_data, PixelsData &pixelsData);

    ModelType model_type_to_native(jint model_type);

    void clear(JNIEnv *env);
}

#endif //GLANCE_JVM_CONVERT_H

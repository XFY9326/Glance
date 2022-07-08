#ifndef GLANCE_JVM_CONVERT_H
#define GLANCE_JVM_CONVERT_H

#include <jni.h>
#include <memory>
#include <vector>
#include "data.h"

namespace JVMConvert {
    using namespace std;

    bool init(JNIEnv *env);

    jobject ml_output_to_jvm(JNIEnv *env, const MLOutput &output);

    bool pixels_data_to_native(JNIEnv *env, jobject pixels_data, PixelsData &pixelsData);

    void clear(JNIEnv *env);
}

#endif //GLANCE_JVM_CONVERT_H

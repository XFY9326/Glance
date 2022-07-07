#include <jni.h>
#include <android/bitmap.h>
#include "log.h"
#include "jvm_convert.h"
#include "ncnn_helper.h"
#include "object_detector.h"

#define LOG_TAG "JNI_INTERFACE"
#define JNI_VERSION JNI_VERSION_1_4

extern "C" JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *) {
    JNIEnv *env;
    if (vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION) != JNI_OK) return JNI_ERR;
    if (!JVMConvert::init(env)) {
        LOG_E(LOG_TAG, "JVM Convert class load error!");
        return JNI_ERR;
    }
    if (NCNNHelper::has_gpu_support() && !NCNNHelper::create_gpu_instance()) {
        LOG_E(LOG_TAG, "NCNN GPU instance create failed!");
    }
    return JNI_VERSION;
}

extern "C" JNIEXPORT void JNI_OnUnload(JavaVM *vm, void *) {
    JNIEnv *env;
    if (vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION) == JNI_OK) {
        JVMConvert::clear(env);
    }
    ObjectDetector::clear_model();
    NCNNHelper::destroy_gpu_instance();
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_io_github_xfy9326_glance_ml_NativeInterface_isGPUInstanceCreated(JNIEnv *, jobject) {
    return NCNNHelper::is_gpu_instance_created() ? JNI_TRUE : JNI_FALSE;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_io_github_xfy9326_glance_ml_NativeInterface_hasGPUSupport(JNIEnv *, jobject) {
    return NCNNHelper::has_gpu_support() ? JNI_TRUE : JNI_FALSE;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_io_github_xfy9326_glance_ml_NativeInterface_isDetectionModelInitialized(JNIEnv *, jobject) {
    return ObjectDetector::is_model_initialized() ? JNI_TRUE : JNI_FALSE;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_io_github_xfy9326_glance_ml_NativeInterface_initDetectionModel(JNIEnv *env, jobject, jobject asset_manager, jstring bin_path, jstring param_bin_path) {
    const char *bin = env->GetStringUTFChars(bin_path, JNI_FALSE);
    const char *param_bin = env->GetStringUTFChars(param_bin_path, JNI_FALSE);

    AAssetManager *mgr = AAssetManager_fromJava(env, asset_manager);
    auto result = ObjectDetector::init_model(mgr, bin, param_bin) ? JNI_TRUE : JNI_FALSE;

    env->ReleaseStringUTFChars(bin_path, bin);
    env->ReleaseStringUTFChars(param_bin_path, param_bin);

    return result;
}

extern "C"
JNIEXPORT jobjectArray JNICALL
Java_io_github_xfy9326_glance_ml_NativeInterface_detectByPixelsData(JNIEnv *env, jobject, jobject pixels_data, jfloat conf_threshold, jfloat iou_threshold) {
    using namespace std;

    PixelsData pixelsData;
    JVMConvert::pixels_data_to_native(env, pixels_data, pixelsData);
    const auto output = ObjectDetector::detect(
            pixelsData, (float) conf_threshold, (float) iou_threshold
    );
    if (output == nullptr) return nullptr;
    return JVMConvert::output_vector_to_jvm(env, output->objects);
}

extern "C"
JNIEXPORT jobjectArray JNICALL
Java_io_github_xfy9326_glance_ml_NativeInterface_detectByBitmap(JNIEnv *env, jobject, jobject bitmap, jfloat conf_threshold, jfloat iou_threshold) {
    using namespace std;

    const auto output = ObjectDetector::detect(
            env, bitmap, (float) conf_threshold, (float) iou_threshold
    );
    if (output == nullptr) return nullptr;
    return JVMConvert::output_vector_to_jvm(env, output->objects);
}

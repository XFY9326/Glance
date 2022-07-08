#include <jni.h>
#include <android/bitmap.h>
#include "log.h"
#include "jvm_convert.h"
#include "ncnn_helper.h"
#include "ml_manager.h"

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
    MLManager::clear_models();
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
Java_io_github_xfy9326_glance_ml_NativeInterface_isModelsInitialized(JNIEnv *, jobject) {
    return MLManager::is_models_initialized() ? JNI_TRUE : JNI_FALSE;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_io_github_xfy9326_glance_ml_NativeInterface_initModels(
        JNIEnv *env, jobject, jobject asset_manager
) {
    AAssetManager *mgr = AAssetManager_fromJava(env, asset_manager);
    return MLManager::init_models(mgr) ? JNI_TRUE : JNI_FALSE;
}

extern "C"
JNIEXPORT jobject JNICALL
Java_io_github_xfy9326_glance_ml_NativeInterface_analyzeImageByPixelsData(
        JNIEnv *env, jobject, jobject pixels_data,
        jfloat conf_threshold, jfloat iou_threshold, jboolean request_caption
) {
    using namespace std;

    PixelsData pixelsData;
    JVMConvert::pixels_data_to_native(env, pixels_data, pixelsData);
    const auto output = MLManager::analyze_image(
            pixelsData, request_caption == JNI_TRUE, (float) conf_threshold, (float) iou_threshold
    );
    return JVMConvert::ml_output_to_jvm(env, *output);
}

extern "C"
JNIEXPORT jobject JNICALL
Java_io_github_xfy9326_glance_ml_NativeInterface_analyzeImageByBitmap(
        JNIEnv *env, jobject, jobject bitmap,
        jfloat conf_threshold, jfloat iou_threshold, jboolean request_caption
) {
    using namespace std;

    const auto output = MLManager::analyze_image(
            env, bitmap, request_caption == JNI_TRUE, (float) conf_threshold, (float) iou_threshold
    );
    return JVMConvert::ml_output_to_jvm(env, *output);
}

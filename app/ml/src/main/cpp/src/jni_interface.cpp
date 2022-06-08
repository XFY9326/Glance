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
    NCNNHelper::destroy_gpu_instance();
    NCNNHelper::blob_pool_allocator.clear();
    NCNNHelper::workspace_pool_allocator.clear();
    ObjectDetector::clear_guide_model();
    ObjectDetector::clear_general_model();
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
Java_io_github_xfy9326_glance_ml_NativeInterface_isGuideModelInitialized(JNIEnv *, jobject) {
    return ObjectDetector::is_guide_model_initialized() ? JNI_TRUE : JNI_FALSE;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_io_github_xfy9326_glance_ml_NativeInterface_isGeneralModelInitialized(JNIEnv *, jobject) {
    return ObjectDetector::is_general_model_initialized() ? JNI_TRUE : JNI_FALSE;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_io_github_xfy9326_glance_ml_NativeInterface_initGuideModel(JNIEnv *env, jobject, jobject asset_manager, jstring bin_path, jstring param_bin_path) {
    const char *bin = env->GetStringUTFChars(bin_path, JNI_FALSE);
    const char *param_bin = env->GetStringUTFChars(param_bin_path, JNI_FALSE);

    AAssetManager *mgr = AAssetManager_fromJava(env, asset_manager);
    auto result = ObjectDetector::init_guide_model(mgr, bin, param_bin) ? JNI_TRUE : JNI_FALSE;

    env->ReleaseStringUTFChars(bin_path, bin);
    env->ReleaseStringUTFChars(param_bin_path, param_bin);

    return result;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_io_github_xfy9326_glance_ml_NativeInterface_initGeneralModel(JNIEnv *env, jobject, jobject asset_manager, jstring bin_path, jstring param_bin_path) {
    const char *bin = env->GetStringUTFChars(bin_path, JNI_FALSE);
    const char *param_bin = env->GetStringUTFChars(param_bin_path, JNI_FALSE);

    AAssetManager *mgr = AAssetManager_fromJava(env, asset_manager);
    auto result = ObjectDetector::init_general_model(mgr, bin, param_bin) ? JNI_TRUE : JNI_FALSE;

    env->ReleaseStringUTFChars(bin_path, bin);
    env->ReleaseStringUTFChars(param_bin_path, param_bin);

    return result;
}

extern "C"
JNIEXPORT jobjectArray JNICALL
Java_io_github_xfy9326_glance_ml_NativeInterface_detectGuideModel(JNIEnv *env, jobject, jobject pixels_data, jboolean enable_gpu, jfloat conf_threshold, jfloat iou_threshold) {
    using namespace std;

    PixelsData pixelsData;
    JVMConvert::to_native(env, pixels_data, pixelsData);
    const shared_ptr<vector<shared_ptr<DetectObject>>> output = ObjectDetector::detect_guide_model(
            pixelsData,
            enable_gpu == JNI_TRUE,
            (float) conf_threshold,
            (float) iou_threshold
    );

    return JVMConvert::to_jvm(env, output);
}

extern "C"
JNIEXPORT jobjectArray JNICALL
Java_io_github_xfy9326_glance_ml_NativeInterface_detectGeneralModel(JNIEnv *env, jobject, jobject bitmap, jboolean enable_gpu, jfloat conf_threshold, jfloat iou_threshold) {
    using namespace std;

    const shared_ptr<vector<shared_ptr<DetectObject>>> output = ObjectDetector::detect_general_model(
            env,
            bitmap,
            enable_gpu == JNI_TRUE,
            (float) conf_threshold,
            (float) iou_threshold
    );

    return JVMConvert::to_jvm(env, output);
}

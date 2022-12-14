#include "jvm_convert.h"
#include "log.h"

#define LOG_TAG "JVM_CONVERT"

namespace JVMConvert {
    using namespace std;

    static jclass detect_object_class;
    static jmethodID detect_object_class_constructor;

    static jfieldID pixels_data_class_width;
    static jfieldID pixels_data_class_height;
    static jfieldID pixels_data_class_stride;
    static jfieldID pixels_data_class_pixels;

    static jclass ml_output_class;
    static jmethodID ml_output_class_constructor;

    bool init(JNIEnv *env) {
        jclass clazz;
        jmethodID method;
        jfieldID field;

        clazz = env->FindClass("io/github/xfy9326/glance/ml/beans/DetectObject");
        if (clazz == nullptr) return false;
        detect_object_class = reinterpret_cast<jclass>(env->NewGlobalRef(clazz));

        method = env->GetMethodID(detect_object_class, "<init>", "(FFFFFI)V");
        if (method == nullptr) return false;
        detect_object_class_constructor = method;

        clazz = env->FindClass("io/github/xfy9326/glance/ml/beans/PixelsData");
        if (clazz == nullptr) return false;

        field = env->GetFieldID(clazz, "width", "I");
        if (field == nullptr) return false;
        pixels_data_class_width = field;

        field = env->GetFieldID(clazz, "height", "I");
        if (field == nullptr) return false;
        pixels_data_class_height = field;

        field = env->GetFieldID(clazz, "stride", "I");
        if (field == nullptr) return false;
        pixels_data_class_stride = field;

        field = env->GetFieldID(clazz, "pixels", "Ljava/nio/ByteBuffer;");
        if (field == nullptr) return false;
        pixels_data_class_pixels = field;

        clazz = env->FindClass("io/github/xfy9326/glance/ml/beans/MLDetectOutput");
        if (clazz == nullptr) return false;
        ml_output_class = reinterpret_cast<jclass>(env->NewGlobalRef(clazz));

        method = env->GetMethodID(ml_output_class, "<init>", "([Lio/github/xfy9326/glance/ml/beans/DetectObject;[I)V");
        if (method == nullptr) return false;
        ml_output_class_constructor = method;

        return true;
    }

    static jobjectArray detect_object_vector_to_jvm(JNIEnv *env, const vector<shared_ptr<DetectObject>> &output) {
        if (env == nullptr) return nullptr;
        jobjectArray object_array = env->NewObjectArray((jsize) output.size(), detect_object_class, nullptr);
        for (int i = 0; i < output.size(); ++i) {
            const shared_ptr<DetectObject> &item = output[i];
            jobject detected_object = env->NewObject(
                    detect_object_class, detect_object_class_constructor,
                    (jfloat) item->left, (jfloat) item->top, (jfloat) item->right, (jfloat) item->bottom,
                    (jfloat) item->confidence, (jint) item->class_id
            );
            env->SetObjectArrayElement(object_array, i, detected_object);
        }
        return object_array;
    }

    jintArray caption_ids_vector_to_jvm(JNIEnv *env, const vector<int> &output) {
        if (env == nullptr) return nullptr;
        jintArray int_array = env->NewIntArray((jsize) output.size());
        env->SetIntArrayRegion(int_array, 0, (jsize) output.size(), (jint *) &output[0]);
        return int_array;
    }

    jobject ml_detect_output_to_jvm(JNIEnv *env, const MLDetectOutput &output) {
        return env->NewObject(
                ml_output_class, ml_output_class_constructor,
                (output.objects != nullptr) ? detect_object_vector_to_jvm(env, *output.objects) : nullptr,
                (output.caption_ids != nullptr) ? caption_ids_vector_to_jvm(env, *output.caption_ids) : nullptr
        );
    }

    bool pixels_data_to_native(JNIEnv *env, jobject pixels_data, PixelsData &pixelsData) {
        if (env == nullptr || pixels_data == nullptr) return false;
        const jint width = env->GetIntField(pixels_data, pixels_data_class_width);
        const jint height = env->GetIntField(pixels_data, pixels_data_class_height);
        const jint stride = env->GetIntField(pixels_data, pixels_data_class_stride);
        jobject buffers = env->GetObjectField(pixels_data, pixels_data_class_pixels);
        auto *pixels = static_cast<unsigned char *>(env->GetDirectBufferAddress(buffers));
        const long long pixels_size = env->GetDirectBufferCapacity(buffers);
        if (stride < width || pixels_size == stride * height) {
            pixelsData.width = width;
            pixelsData.height = height;
            pixelsData.stride = stride;
            pixelsData.pixels = pixels;
            return true;
        } else {
            LOG_E(LOG_TAG, "Wrong pixels size! Required size: %lld  Input size: w %d h %d s %d", pixels_size, width, height, stride);
            return false;
        }
    }

    void clear(JNIEnv *env) {
        if (detect_object_class != nullptr) env->DeleteGlobalRef(detect_object_class);
        detect_object_class = nullptr;
        detect_object_class_constructor = nullptr;

        pixels_data_class_width = nullptr;
        pixels_data_class_height = nullptr;
        pixels_data_class_stride = nullptr;
        pixels_data_class_pixels = nullptr;
    }
}
# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html
cmake_minimum_required(VERSION 3.18.1)

project("ml")

# NCNN
set(ncnn_DIR ${CMAKE_SOURCE_DIR}/ncnn/${ANDROID_ABI}/lib/cmake/ncnn)
find_package(ncnn REQUIRED)

add_library(
        ml
        SHARED
        src/data.h
        src/debug.h
        src/gru.h
        src/gru.cpp
        src/image_caption.h
        src/image_caption.cpp
        src/jni_interface.cpp
        src/jvm_convert.h
        src/jvm_convert.cpp
        src/log.h
        src/ml_manager.h
        src/ml_manager.cpp
        src/model.h
        src/ncnn_helper.h
        src/ncnn_helper.cpp
        src/object_detector.h
        src/object_detector.cpp
        src/utils.h
        src/utils.cpp
        src/yolov5.h
        src/yolov5.cpp
)

# Android NDK Log
find_library(log-lib log)

# Link libraries
target_link_libraries(
        ml
        ncnn
        jnigraphics
        ${log-lib})

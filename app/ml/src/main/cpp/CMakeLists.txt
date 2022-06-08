# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html
cmake_minimum_required(VERSION 3.18.1)

project("ml")

# NCNN
set(ncnn_DIR ${CMAKE_SOURCE_DIR}/ncnn/${ANDROID_ABI}/lib/cmake/ncnn)
find_package(ncnn REQUIRED)

# ML lib
aux_source_directory(src SRC_LIST)
add_library(ml SHARED ${SRC_LIST})

# Android NDK Log
find_library(log-lib log)

# Link libraries
target_link_libraries(
        ml
        ncnn
        jnigraphics
        ${log-lib})

# Settings
if (CMAKE_BUILD_TYPE STREQUAL "Release")
    set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -Os -ffunction-sections -fdata-sections -fvisibility=hidden -fvisibility-inlines-hidden")
    set(CMAKE_CXX_FLAGS "${CMAKE_C_FLAGS}")
    SET_TARGET_PROPERTIES(ml PROPERTIES LINK_FLAGS "-Wl,-exclude-libs,ALL -Wl,--gc-sections -Wl,--strip-all")
endif ()

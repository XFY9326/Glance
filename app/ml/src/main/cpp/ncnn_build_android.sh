#!/usr/bin/env bash

ANDROID_NDK=/root/android-ndk-r24
CUSTOM_FLAG="-DNCNN_PIXEL_ROTATE=OFF -DNCNN_PIXEL_AFFINE=OFF -DNCNN_STRING=OFF -DNCNN_STDIO=OFF -DNCNN_DISABLE_RTTI=ON -DNCNN_DISABLE_EXCEPTION=ON -DNCNN_C_API=OFF -DNCNN_PIXEL_DRAWING=OFF"

##### android armv7 vulkan
mkdir -p build-android-armv7-vulkan
pushd build-android-armv7-vulkan || exit
cmake -DCMAKE_TOOLCHAIN_FILE=$ANDROID_NDK/build/cmake/android.toolchain.cmake -DANDROID_ABI="armeabi-v7a" -DANDROID_ARM_NEON=ON -DANDROID_PLATFORM=android-24 -DNCNN_VULKAN=ON $CUSTOM_FLAG ..
make -j8 && make install
popd || exit

##### android aarch64 vulkan
mkdir -p build-android-aarch64-vulkan
pushd build-android-aarch64-vulkan || exit
cmake -DCMAKE_TOOLCHAIN_FILE=$ANDROID_NDK/build/cmake/android.toolchain.cmake -DANDROID_ABI="arm64-v8a" -DANDROID_PLATFORM=android-24 -DNCNN_VULKAN=ON $CUSTOM_FLAG ..
make -j8 && make install
popd || exit

##### android x86 vulkan
mkdir -p build-android-x86-vulkan
pushd build-android-x86-vulkan || exit
cmake -DCMAKE_TOOLCHAIN_FILE=$ANDROID_NDK/build/cmake/android.toolchain.cmake -DANDROID_ABI="x86" -DANDROID_PLATFORM=android-24 -DNCNN_VULKAN=ON $CUSTOM_FLAG ..
make -j8
make install
popd || exit

##### android x86_64 vulkan
mkdir -p build-android-x86_64-vulkan
pushd  build-android-x86_64-vulkan || exit
cmake -DCMAKE_TOOLCHAIN_FILE=$ANDROID_NDK/build/cmake/android.toolchain.cmake -DANDROID_ABI="x86_64" -DANDROID_PLATFORM=android-24 -DNCNN_VULKAN=ON $CUSTOM_FLAG ..
make -j8
make install
popd || exit

NAME=ncnn
ANDROIDPKGNAME=${NAME}-android-vulkan

rm -rf $ANDROIDPKGNAME
mkdir -p $ANDROIDPKGNAME
mkdir -p $ANDROIDPKGNAME/armeabi-v7a
mkdir -p $ANDROIDPKGNAME/arm64-v8a
mkdir -p $ANDROIDPKGNAME/x86
mkdir -p $ANDROIDPKGNAME/x86_64

echo "ncnn" > $ANDROIDPKGNAME/pkgInfo.txt
echo "platform: android-vulkan" >> $ANDROIDPKGNAME/pkgInfo.txt
echo "ndk: ndk-r24" >> $ANDROIDPKGNAME/pkgInfo.txt
echo "tag: "$(git describe --abbrev=0 --tags) >> $ANDROIDPKGNAME/pkgInfo.txt
echo "commit id: "$(git rev-parse --short HEAD) >> $ANDROIDPKGNAME/pkgInfo.txt
echo "cmake custom flags: $CUSTOM_FLAG" >> $ANDROIDPKGNAME/pkgInfo.txt

cp -r build-android-armv7-vulkan/install/lib $ANDROIDPKGNAME/armeabi-v7a/
cp -r build-android-armv7-vulkan/install/include $ANDROIDPKGNAME/armeabi-v7a/
rm -rf  $ANDROIDPKGNAME/armeabi-v7a/lib/pkgconfig

cp -r build-android-aarch64-vulkan/install/lib $ANDROIDPKGNAME/arm64-v8a/
cp -r build-android-aarch64-vulkan/install/include $ANDROIDPKGNAME/arm64-v8a/
rm -rf  $ANDROIDPKGNAME/arm64-v8a/lib/pkgconfig

cp -r build-android-x86-vulkan/install/lib $ANDROIDPKGNAME/x86/
cp -r build-android-x86-vulkan/install/include $ANDROIDPKGNAME/x86/
rm -rf  $ANDROIDPKGNAME/x86/lib/pkgconfig

cp -r build-android-x86_64-vulkan/install/lib $ANDROIDPKGNAME/x86_64/
cp -r build-android-x86_64-vulkan/install/include $ANDROIDPKGNAME/x86_64/
rm -rf  $ANDROIDPKGNAME/x86_64/lib/pkgconfig

rm -f $ANDROIDPKGNAME.zip
zip -q -9 -r $ANDROIDPKGNAME.zip $ANDROIDPKGNAME

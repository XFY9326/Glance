package io.github.xfy9326.glance.io

import io.github.xfy9326.atools.io.AppDir
import io.github.xfy9326.atools.io.utils.asParentOf
import java.io.File

object FileManager {
    private const val FILE_CAPTURED_PICTURE = "captured_picture"
    private const val DIR_CAPTURED = "Captured"

    val capturedCachePicture: File
        get() = AppDir.externalCacheDir.asParentOf(DIR_CAPTURED, FILE_CAPTURED_PICTURE)

}
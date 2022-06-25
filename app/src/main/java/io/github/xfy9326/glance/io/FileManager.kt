package io.github.xfy9326.glance.io

import android.net.Uri
import io.github.xfy9326.atools.io.AppDir
import io.github.xfy9326.atools.io.utils.asParentOf
import io.github.xfy9326.atools.io.utils.getUriByFileProvider
import io.github.xfy9326.atools.io.utils.preparedParentFolder
import io.github.xfy9326.glance.BuildConfig

object FileManager {
    private const val FILE_PROVIDER_AUTH = "${BuildConfig.APPLICATION_ID}.file.provider"
    private const val FILE_CAPTURED_IMAGE = "captured"
    private const val DIR_IMAGE = "image"

    val capturedCacheImageUri: Uri by lazy {
        AppDir.externalCacheDir
            .asParentOf(DIR_IMAGE, FILE_CAPTURED_IMAGE)
            .preparedParentFolder()
            .getUriByFileProvider(FILE_PROVIDER_AUTH)
    }
}
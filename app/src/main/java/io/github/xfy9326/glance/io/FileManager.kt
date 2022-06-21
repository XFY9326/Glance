package io.github.xfy9326.glance.io

import android.graphics.Bitmap
import android.net.Uri
import io.github.xfy9326.atools.io.AppDir
import io.github.xfy9326.atools.io.okio.forceReadBitmap
import io.github.xfy9326.atools.io.okio.source
import io.github.xfy9326.atools.io.okio.useBuffer
import io.github.xfy9326.atools.io.utils.asParentOf
import io.github.xfy9326.atools.io.utils.preparedParentFolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object FileManager {
    private const val FILE_CAPTURED_IMAGE = "captured"
    private const val DIR_IMAGE = "image"

    val capturedCacheImageUri: Uri by lazy {
        AppDir.externalCacheDir
            .asParentOf(DIR_IMAGE, FILE_CAPTURED_IMAGE)
            .preparedParentFolder()
            .getUriByFileProvider()
    }

    suspend fun readBitmap(uri: Uri): Result<Bitmap> = withContext(Dispatchers.IO) {
        runCatching {
            uri.source().useBuffer { forceReadBitmap() }
        }
    }
}
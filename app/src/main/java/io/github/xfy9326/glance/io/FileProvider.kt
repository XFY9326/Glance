package io.github.xfy9326.glance.io

import android.net.Uri
import androidx.core.content.FileProvider
import io.github.xfy9326.atools.core.AppContext
import io.github.xfy9326.glance.BuildConfig
import java.io.File

private const val FILE_PROVIDER_AUTH = "${BuildConfig.APPLICATION_ID}.file.provider"

fun File.getUriByFileProvider(displayName: String? = null): Uri =
    if (displayName == null) {
        FileProvider.getUriForFile(AppContext, FILE_PROVIDER_AUTH, this)
    } else {
        FileProvider.getUriForFile(AppContext, FILE_PROVIDER_AUTH, this, displayName)
    }
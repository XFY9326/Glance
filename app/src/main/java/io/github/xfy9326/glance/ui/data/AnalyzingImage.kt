package io.github.xfy9326.glance.ui.data

import android.net.Uri

data class AnalyzingImage(
    val uri: Uri,
    val cacheKey: String? = System.currentTimeMillis().toString()
)
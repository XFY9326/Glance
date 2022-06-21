package io.github.xfy9326.glance.ui.screen.main

import android.net.Uri
import androidx.lifecycle.ViewModel
import io.github.xfy9326.glance.io.FileManager

class MainViewModel : ViewModel() {
    val capturedCacheImageUri: Uri
        get() = FileManager.capturedCacheImageUri
}
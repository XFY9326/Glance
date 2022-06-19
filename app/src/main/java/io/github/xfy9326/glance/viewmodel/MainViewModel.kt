package io.github.xfy9326.glance.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import io.github.xfy9326.atools.io.utils.preparedParentFolder
import io.github.xfy9326.glance.io.FileManager
import io.github.xfy9326.glance.io.getUriByFileProvider

class MainViewModel : ViewModel() {
    val capturedCacheImageUri: Uri by lazy {
        FileManager.capturedCachePicture.preparedParentFolder().getUriByFileProvider()
    }
}
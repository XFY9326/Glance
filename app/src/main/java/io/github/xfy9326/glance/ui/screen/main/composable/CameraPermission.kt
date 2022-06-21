package io.github.xfy9326.glance.ui.screen.main.composable

import android.Manifest
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import io.github.xfy9326.glance.R
import io.github.xfy9326.glance.tools.isPermissionGranted
import io.github.xfy9326.glance.ui.base.rememberLauncherForRequestPermission
import io.github.xfy9326.glance.ui.common.CameraPermissionDialog
import kotlinx.coroutines.launch

@Composable
fun rememberCameraPermissionRequest(
    snackbarHostState: SnackbarHostState,
    onPermissionGranted: () -> Unit
): CameraPermissionRequest {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val dialogState = remember { mutableStateOf(false) }
    val permissionRequest = rememberLauncherForRequestPermission {
        if (it) {
            onPermissionGranted()
        } else {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    context.getString(R.string.permission_not_granted, context.getString(R.string.camera_permission))
                )
            }
        }
    }
    if (dialogState.value) {
        CameraPermissionDialog(onDismissRequest = { dialogState.value = false }) {
            permissionRequest.launch(Manifest.permission.CAMERA)
        }
    }
    return object : CameraPermissionRequest {
        override fun requestPermission() {
            if (context.isPermissionGranted(Manifest.permission.CAMERA)) {
                onPermissionGranted()
            } else {
                dialogState.value = true
            }
        }
    }
}

interface CameraPermissionRequest {
    fun requestPermission()
}
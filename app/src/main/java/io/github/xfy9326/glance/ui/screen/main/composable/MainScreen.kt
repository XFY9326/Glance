package io.github.xfy9326.glance.ui.screen.main.composable

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.xfy9326.glance.R
import io.github.xfy9326.glance.tools.MIME_IMAGE
import io.github.xfy9326.glance.ui.base.rememberLauncherForGetContent
import io.github.xfy9326.glance.ui.base.rememberLauncherForTakePicture
import io.github.xfy9326.glance.ui.common.AboutDialog
import io.github.xfy9326.glance.ui.screen.main.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel(),
    onNavigateToGuide: () -> Unit,
    onNavigateToAnalysis: (Uri) -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val aboutDialog = remember { mutableStateOf(false) }
    val takePicture = createTakePictureLauncher(scaffoldState.snackbarHostState) {
        onNavigateToAnalysis(viewModel.capturedCacheImageUri)
    }
    val selectPicture = createSelectPictureLauncher(scaffoldState.snackbarHostState) {
        onNavigateToAnalysis(it)
    }
    val guideCameraPermission = rememberCameraPermissionRequest(
        snackbarHostState = scaffoldState.snackbarHostState,
        onPermissionGranted = { onNavigateToGuide() }
    )
    val cameraPhotoCameraPermission = rememberCameraPermissionRequest(
        snackbarHostState = scaffoldState.snackbarHostState,
        onPermissionGranted = { takePicture.launch(viewModel.capturedCacheImageUri) }
    )
    MainContent(
        scaffoldState = scaffoldState,
        onGuideClick = {
            guideCameraPermission.requestPermission()
        },
        onCameraPhotoAnalysisClick = {
            cameraPhotoCameraPermission.requestPermission()
        },
        onLocalImageAnalysisClick = {
            selectPicture.launch(MIME_IMAGE)
        },
        onSettingsClick = {

        },
        onHelpClick = {

        },
        onAboutClick = {
            aboutDialog.value = true
        }
    )
    if (aboutDialog.value) {
        AboutDialog(onDismissRequest = { aboutDialog.value = false })
    }
}

@Composable
private fun createSelectPictureLauncher(
    snackbarHostState: SnackbarHostState,
    onSelected: (Uri) -> Unit
): ManagedActivityResultLauncher<String, Uri?> {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    return rememberLauncherForGetContent {
        if (it != null) {
            onSelected(it)
        } else {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(context.getString(R.string.cancel_selecting_picture))
            }
        }
    }
}

@Composable
private fun createTakePictureLauncher(
    snackbarHostState: SnackbarHostState,
    onCaptured: () -> Unit
): ManagedActivityResultLauncher<Uri, Boolean> {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    return rememberLauncherForTakePicture {
        if (it) {
            onCaptured()
        } else {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(context.getString(R.string.cancel_taking_picture))
            }
        }
    }
}
package io.github.xfy9326.glance.ui.activity

import android.Manifest
import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.xfy9326.glance.R
import io.github.xfy9326.glance.activity.AnalysisActivity
import io.github.xfy9326.glance.activity.GuideActivity
import io.github.xfy9326.glance.tools.MimeType
import io.github.xfy9326.glance.tools.isPermissionGranted
import io.github.xfy9326.glance.tools.startActivity
import io.github.xfy9326.glance.ui.base.ApplicationIcon
import io.github.xfy9326.glance.ui.dialog.DialogController
import io.github.xfy9326.glance.ui.dialog.createAboutDialog
import io.github.xfy9326.glance.ui.dialog.createCameraPermissionDialog
import io.github.xfy9326.glance.ui.theme.AppTheme
import io.github.xfy9326.glance.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private val LocalScaffoldState = compositionLocalOf<ScaffoldState> { error("CompositionLocal ScaffoldState not present") }
private val LocalCoroutineScope = compositionLocalOf<CoroutineScope> { error("CompositionLocal CoroutineScope not present") }

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun MainActivityContent() {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    AppTheme {
        Scaffold(scaffoldState = scaffoldState) {
            CompositionLocalProvider(
                LocalScaffoldState provides scaffoldState,
                LocalCoroutineScope provides coroutineScope
            ) {
                Screen(paddingValues = it)
            }
        }
    }
}

@Composable
private fun Screen(paddingValues: PaddingValues) {
    val contentScrollState = rememberScrollState()
    val configuration = LocalConfiguration.current
    val contentWidthFraction = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 0.6f else 0.9f
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(contentScrollState),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .systemBarsPadding()
                .fillMaxHeight()
                .fillMaxWidth(fraction = contentWidthFraction)
        ) {
            Header()
            Spacer(modifier = Modifier.height(8.dp))
            Functions()
            Spacer(modifier = Modifier.height(16.dp))
            Settings()
        }
    }
}

@Composable
private fun Header() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ApplicationIcon(modifier = Modifier.size(80.dp))
        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun Functions() {
    val context = LocalContext.current
    val viewModel = viewModel<MainViewModel>()
    val guideCameraPermissionDialog = createRequestCameraPermissionDialog {
        context.startActivity<GuideActivity>()
    }
    val takePicture = createTakingPictureContract()
    val cameraPhotoCameraPermissionDialog = createRequestCameraPermissionDialog {
        takePicture.launch(viewModel.capturedCacheImageUri)
    }
    val choosePicture = createSelectPictureContract()
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        FunctionCard(
            icon = ImageVector.vectorResource(id = R.drawable.ic_blind_24),
            title = stringResource(id = R.string.guide),
            summary = stringResource(id = R.string.guide_summary)
        ) {
            if (context.isPermissionGranted(Manifest.permission.CAMERA)) {
                context.startActivity<GuideActivity>()
            } else {
                guideCameraPermissionDialog.show()
            }
        }
        FunctionCard(
            icon = Icons.Default.Radar,
            title = stringResource(id = R.string.camera_photo_analysis),
            summary = stringResource(id = R.string.camera_photo_analysis_summary)
        ) {
            if (context.isPermissionGranted(Manifest.permission.CAMERA)) {
                takePicture.launch(viewModel.capturedCacheImageUri)
            } else {
                cameraPhotoCameraPermissionDialog.show()
            }
        }
        FunctionCard(
            icon = Icons.Default.ImageSearch,
            title = stringResource(id = R.string.local_image_analysis),
            summary = stringResource(id = R.string.local_image_analysis_summary)
        ) {
            choosePicture.launch(MimeType.IMAGE)
        }
    }
}

@Composable
private fun createRequestCameraPermissionDialog(onGranted: () -> Unit): DialogController {
    val context = LocalContext.current
    val scaffoldState = LocalScaffoldState.current
    val coroutineScope = LocalCoroutineScope.current
    val cameraPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            onGranted()
        } else {
            coroutineScope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    context.getString(R.string.permission_not_granted, context.getString(R.string.camera_permission))
                )
            }
        }
    }
    return createCameraPermissionDialog {
        cameraPermission.launch(Manifest.permission.CAMERA)
    }
}

@Composable
private fun createTakingPictureContract(): ManagedActivityResultLauncher<Uri, Boolean> {
    val context = LocalContext.current
    val scaffoldState = LocalScaffoldState.current
    val coroutineScope = LocalCoroutineScope.current
    val viewModel = viewModel<MainViewModel>()
    return rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            AnalysisActivity.launch(context, viewModel.capturedCacheImageUri)
        } else {
            coroutineScope.launch {
                scaffoldState.snackbarHostState.showSnackbar(context.getString(R.string.cancel_taking_picture))
            }
        }
    }
}

@Composable
private fun createSelectPictureContract(): ManagedActivityResultLauncher<String, Uri?> {
    val context = LocalContext.current
    val scaffoldState = LocalScaffoldState.current
    val coroutineScope = LocalCoroutineScope.current
    return rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            AnalysisActivity.launch(context, it)
        } else {
            coroutineScope.launch {
                scaffoldState.snackbarHostState.showSnackbar(context.getString(R.string.cancel_selecting_picture))
            }
        }
    }
}

@Composable
private fun Settings() {
    val aboutDialog = createAboutDialog()
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        FunctionItem(
            icon = Icons.Default.Settings,
            title = stringResource(id = R.string.settings)
        ) {

        }
        FunctionItem(
            icon = Icons.Default.HelpCenter,
            title = stringResource(id = R.string.help)
        ) {

        }
        FunctionItem(
            icon = Icons.Default.Info,
            title = stringResource(id = R.string.about)
        ) {
            aboutDialog.show()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun FunctionCard(icon: ImageVector, title: String, summary: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(84.dp)
            .semantics(mergeDescendants = true) {}
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 18.dp, vertical = 4.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(36.dp)
            )
            Column(
                modifier = Modifier.padding(start = 15.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = title, fontSize = 16.sp)
                Text(text = summary, fontSize = 14.sp)
            }
        }
    }
}

@Composable
private fun FunctionItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .semantics(mergeDescendants = true) {},
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(30.dp)
        )
        Text(
            text = title,
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 20.dp)
        )
    }
}
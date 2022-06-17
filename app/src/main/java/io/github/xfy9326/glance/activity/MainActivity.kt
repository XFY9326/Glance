package io.github.xfy9326.glance.activity

import android.Manifest
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.rememberScaffoldState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import io.github.xfy9326.glance.R
import io.github.xfy9326.glance.tools.startActivity
import io.github.xfy9326.glance.ui.base.ApplicationIcon
import io.github.xfy9326.glance.ui.base.FunctionCard
import io.github.xfy9326.glance.ui.base.FunctionItem
import io.github.xfy9326.glance.ui.base.prepareSplashScreen
import io.github.xfy9326.glance.ui.dialog.createAboutDialog
import io.github.xfy9326.glance.ui.dialog.createCameraPermissionDialog
import io.github.xfy9326.glance.ui.theme.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        prepareSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            Content()
        }
    }
}

private val LocalScaffoldState = compositionLocalOf<ScaffoldState> { error("No local scaffold state available!") }
private val LocalCoroutineScope = compositionLocalOf<CoroutineScope> { error("No local coroutine scope available!") }

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
private fun Content() {
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
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
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
                .fillMaxWidth(fraction = if (isLandscape) 0.6f else 0.9f)
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
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        FunctionGuideCard()
        FunctionCameraPhotoAnalysisCard()
        FunctionLocalImageAnalysisCard()
    }
}

@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun FunctionGuideCard() {
    val context = LocalContext.current
    val scaffoldState = LocalScaffoldState.current
    val coroutineScope = LocalCoroutineScope.current
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA) {
        if (it) {
            context.startActivity<GuideActivity>()
        } else {
            coroutineScope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    context.getString(R.string.permission_not_granted, context.getString(R.string.camera_permission))
                )
            }
        }
    }
    val cameraPermissionDialog = createCameraPermissionDialog {
        cameraPermissionState.launchPermissionRequest()
    }
    FunctionCard(
        icon = ImageVector.vectorResource(id = R.drawable.ic_blind_24),
        title = stringResource(id = R.string.guide),
        summary = stringResource(id = R.string.guide_summary)
    ) {
        if (cameraPermissionState.status.isGranted) {
            context.startActivity<GuideActivity>()
        } else {
            cameraPermissionDialog.show()
        }
    }
}

@Composable
fun FunctionCameraPhotoAnalysisCard() {
    val context = LocalContext.current
    FunctionCard(
        icon = Icons.Default.Camera,
        title = stringResource(id = R.string.camera_photo_analysis),
        summary = stringResource(id = R.string.camera_photo_analysis_summary)
    ) {
        context.startActivity<CameraActivity>()
    }
}

@Composable
fun FunctionLocalImageAnalysisCard() {
    val context = LocalContext.current
    FunctionCard(
        icon = Icons.Default.ImageSearch,
        title = stringResource(id = R.string.local_image_analysis),
        summary = stringResource(id = R.string.local_image_analysis_summary)
    ) {
        context.startActivity<AnalysisActivity>()
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
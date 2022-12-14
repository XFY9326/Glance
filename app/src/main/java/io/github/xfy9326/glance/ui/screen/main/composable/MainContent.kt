package io.github.xfy9326.glance.ui.screen.main.composable

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.xfy9326.glance.R
import io.github.xfy9326.glance.ui.common.ApplicationIcon
import io.github.xfy9326.glance.ui.theme.AppTheme

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
private fun PreviewMainScreen() {
    AppTheme {
        MainContent(
            scaffoldState = rememberScaffoldState(),
            onSceneClick = {},
            onFinderClick = {},
            onCameraPhotoAnalysisClick = {},
            onLocalImageAnalysisClick = {},
            onAboutClick = {}
        )
    }
}

@Composable
fun MainContent(
    scaffoldState: ScaffoldState,
    onSceneClick: () -> Unit,
    onFinderClick: () -> Unit,
    onCameraPhotoAnalysisClick: () -> Unit,
    onLocalImageAnalysisClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    val contentScrollState = rememberScrollState()
    val configuration = LocalConfiguration.current
    val contentWidthFraction = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 0.6f else 0.9f

    Scaffold(scaffoldState = scaffoldState) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(contentScrollState),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .systemBarsPadding()
                    .fillMaxHeight()
                    .fillMaxWidth(fraction = contentWidthFraction)
            ) {
                Header()
                Spacer(modifier = Modifier.height(16.dp))
                Functions(
                    onSceneClick = onSceneClick,
                    onFinderClick = onFinderClick,
                    onCameraPhotoAnalysisClick = onCameraPhotoAnalysisClick,
                    onLocalImageAnalysisClick = onLocalImageAnalysisClick
                )
                Spacer(modifier = Modifier.height(28.dp))
                Others(
                    onAboutClick = onAboutClick
                )
            }
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
private fun Functions(
    onSceneClick: () -> Unit,
    onFinderClick: () -> Unit,
    onCameraPhotoAnalysisClick: () -> Unit,
    onLocalImageAnalysisClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        FunctionCard(
            icon = ImageVector.vectorResource(id = R.drawable.ic_detection_24),
            title = stringResource(id = R.string.scene),
            summary = stringResource(id = R.string.scene_summary),
            onClick = onSceneClick
        )
        FunctionCard(
            icon = Icons.Default.Radar,
            title = stringResource(id = R.string.finder),
            summary = stringResource(id = R.string.finder_summary),
            onClick = onFinderClick
        )
        FunctionCard(
            icon = ImageVector.vectorResource(id = R.drawable.ic_blind_24),
            title = stringResource(id = R.string.camera_photo_analysis),
            summary = stringResource(id = R.string.camera_photo_analysis_summary),
            onClick = onCameraPhotoAnalysisClick
        )
        FunctionCard(
            icon = Icons.Default.ImageSearch,
            title = stringResource(id = R.string.local_image_analysis),
            summary = stringResource(id = R.string.local_image_analysis_summary),
            onClick = onLocalImageAnalysisClick
        )
    }
}

@Composable
private fun Others(
    onAboutClick: () -> Unit
) {
    Box(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        FunctionItem(
            icon = Icons.Default.Info,
            title = stringResource(id = R.string.about),
            onClick = onAboutClick
        )
    }
}

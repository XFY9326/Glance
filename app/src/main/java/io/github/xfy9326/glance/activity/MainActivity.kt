package io.github.xfy9326.glance.activity

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.xfy9326.glance.R
import io.github.xfy9326.glance.ui.base.FunctionCard
import io.github.xfy9326.glance.ui.base.FunctionItem
import io.github.xfy9326.glance.ui.base.prepareSplashScreen
import io.github.xfy9326.glance.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepareSplashScreen()
        setContent {
            Content()
        }
    }
}

@Composable
private fun Content() {
    val contentScrollState = rememberScrollState()
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    AppTheme {
        Scaffold {
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

    }
}

@Composable
private fun Header() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = stringResource(id = R.string.app_icon),
            modifier = Modifier.size(80.dp),
            tint = AppTheme.launcherIconColor
        )
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
        FunctionCard(
            icon = ImageVector.vectorResource(id = R.drawable.ic_blind_24),
            title = stringResource(id = R.string.guide),
            summary = stringResource(id = R.string.guide_summary)
        ) {

        }
        FunctionCard(
            icon = Icons.Default.PhotoCamera,
            title = stringResource(id = R.string.camera_photo_analysis),
            summary = stringResource(id = R.string.camera_photo_analysis_summary)
        ) {

        }
        FunctionCard(
            icon = Icons.Default.ImageSearch,
            title = stringResource(id = R.string.local_image_analysis),
            summary = stringResource(id = R.string.local_image_analysis_summary)
        ) {

        }
    }
}

@Composable
private fun Settings() {
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

        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
private fun Preview() {
    Content()
}
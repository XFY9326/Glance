package io.github.xfy9326.glance.ui.screen.guide

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import io.github.xfy9326.glance.ui.theme.AppTheme

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
private fun PreviewGuideScreen() {
    AppTheme {
        GuideScreenContent(
            scaffoldState = rememberScaffoldState()
        )
    }
}

@Composable
fun GuideScreen() {
    val scaffoldState = rememberScaffoldState()
    GuideScreenContent(
        scaffoldState = scaffoldState
    )
}

@Composable
fun GuideScreenContent(
    scaffoldState: ScaffoldState
) {
    Scaffold(scaffoldState = scaffoldState) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {

        }
    }
}
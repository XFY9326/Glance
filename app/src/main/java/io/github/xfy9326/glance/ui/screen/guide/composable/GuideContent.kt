package io.github.xfy9326.glance.ui.screen.guide.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import io.github.xfy9326.glance.R
import io.github.xfy9326.glance.ui.common.SimpleTopAppToolBar
import io.github.xfy9326.glance.ui.theme.AppTheme

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
private fun PreviewGuideContent() {
    AppTheme {
        GuideContent(
            scaffoldState = rememberScaffoldState(),
            onBackPressed = {}
        )
    }
}

@Composable
fun GuideContent(
    scaffoldState: ScaffoldState,
    onBackPressed: () -> Unit
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            SimpleTopAppToolBar(
                title = stringResource(id = R.string.guide),
                onBackPressed = onBackPressed
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .navigationBarsPadding()
                .fillMaxSize()
        ) {

        }
    }
}
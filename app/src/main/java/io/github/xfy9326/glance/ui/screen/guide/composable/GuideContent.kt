package io.github.xfy9326.glance.ui.screen.guide.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
            GuideTopAppBar(
                title = stringResource(id = R.string.guide),
                onBackPressed = onBackPressed
            )
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {

        }
    }
}
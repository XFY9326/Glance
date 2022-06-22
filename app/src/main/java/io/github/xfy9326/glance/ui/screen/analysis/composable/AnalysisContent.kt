package io.github.xfy9326.glance.ui.screen.analysis.composable

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import io.github.xfy9326.glance.R
import io.github.xfy9326.glance.ui.base.AnalyzingImage
import io.github.xfy9326.glance.ui.common.DividedLayout
import io.github.xfy9326.glance.ui.common.SimpleTopAppToolBar
import io.github.xfy9326.glance.ui.theme.AppTheme

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
private fun PreviewAnalysisContent() {
    AppTheme {
        AnalysisScreenContent(
            scaffoldState = rememberScaffoldState(),
            image = AnalyzingImage(Uri.EMPTY),
            analyzeText = "Loading ...",
            onBackPressed = {}
        )
    }
}

@Composable
fun AnalysisScreenContent(
    scaffoldState: ScaffoldState,
    image: AnalyzingImage,
    analyzeText: String,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            SimpleTopAppToolBar(
                title = stringResource(id = R.string.image_analysis),
                onBackPressed = onBackPressed
            )
        }
    ) {
        DividedLayout(
            modifier = Modifier
                .padding(it)
                .navigationBarsPadding()
                .fillMaxSize(),
            weightUpStart = 0.7f,
            weightDownEnd = 0.3f,
            contentUpStart = {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .crossfade(true)
                        .data(image.uri)
                        .memoryCacheKey(image.cacheKey)
                        .diskCacheKey(image.cacheKey)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(4.dp)
                        .fillMaxSize()
                )
            },
            contentDownEnd = {
                Text(
                    text = analyzeText,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(4.dp)
                )
            }
        )
    }
}
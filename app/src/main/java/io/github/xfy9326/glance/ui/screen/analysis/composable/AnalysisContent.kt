package io.github.xfy9326.glance.ui.screen.analysis.composable

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import io.github.xfy9326.glance.ui.common.DividedLayout
import io.github.xfy9326.glance.ui.common.SimpleTopAppToolBar
import io.github.xfy9326.glance.ui.data.AnalysisResult
import io.github.xfy9326.glance.ui.data.AnalyzingImage
import io.github.xfy9326.glance.ui.theme.AppTheme
import kotlin.math.roundToInt

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
private fun PreviewAnalysisContent() {
    AppTheme {
        AnalysisScreenContent(
            scaffoldState = rememberScaffoldState(),
            image = AnalyzingImage(Uri.EMPTY),
            analysisResult = AnalysisResult.Processing,
            onBackPressed = {}
        )
    }
}

@Composable
fun AnalysisScreenContent(
    scaffoldState: ScaffoldState,
    image: AnalyzingImage,
    analysisResult: AnalysisResult,
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
                    contentDescription = stringResource(id = R.string.image_being_analyzed),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(4.dp)
                        .fillMaxSize()
                )
            },
            contentDownEnd = {
                when (analysisResult) {
                    is AnalysisResult.Processing -> {
                        Text(
                            text = stringResource(id = R.string.image_processing),
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(4.dp)
                        )
                    }
                    is AnalysisResult.Success -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = 6.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            analysisResult.analysisItem.forEach { item ->
                                Text(
                                    text = stringResource(
                                        id = R.string.analysis_results_text,
                                        item.classText,
                                        (item.reliability * 100).roundToInt()
                                    ),
                                )
                            }
                        }
                    }
                    AnalysisResult.ImageLoadFailed -> {
                        Text(
                            text = stringResource(id = R.string.image_load_failed),
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(4.dp)
                        )
                    }
                    AnalysisResult.ModelLoadFailed -> {
                        Text(
                            text = stringResource(id = R.string.model_init_failed),
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(4.dp)
                        )
                    }
                }
            }
        )
    }
}
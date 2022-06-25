package io.github.xfy9326.glance.ui.screen.analysis.composable

import android.net.Uri
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import io.github.xfy9326.glance.R
import io.github.xfy9326.glance.ui.common.*
import io.github.xfy9326.glance.ui.data.AnalysisResult
import io.github.xfy9326.glance.ui.data.AnalyzingImage
import io.github.xfy9326.glance.ui.theme.AppTheme

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
private fun PreviewAnalysisContent() {
    AppTheme {
        AnalysisScreenContent(
            scaffoldState = rememberScaffoldState(),
            image = AnalyzingImage(Uri.EMPTY),
            analysisResult = AnalysisResult.Processing,
            onBackPressed = {},
            onImageClick = {}
        )
    }
}

@Composable
fun AnalysisScreenContent(
    scaffoldState: ScaffoldState,
    image: AnalyzingImage,
    analysisResult: AnalysisResult,
    onBackPressed: () -> Unit,
    onImageClick: () -> Unit
) {
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
            rowDividerContent = {
                Divider(
                    direction = DividerDirection.Vertical,
                    thickness = 2.dp,
                )
            },
            columnDividerContent = {
                Divider(
                    direction = DividerDirection.Horizontal,
                    thickness = 2.dp,
                )
            },
            contentUpStart = {
                ImageContent(
                    modifier = Modifier.align(Alignment.Center),
                    image = image,
                    onClick = onImageClick
                )
                if (analysisResult is AnalysisResult.Success) {
                    ImageObjectBoxLayer(
                        imageSize = Size(analysisResult.imageWidth.toFloat(), analysisResult.imageHeight.toFloat()),
                        imageObjects = analysisResult.imageObject,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                    )
                }
            },
            contentDownEnd = {
                AnalysisResultContent(
                    modifier = Modifier.align(Alignment.Center),
                    analysisResult = analysisResult
                )
            }
        )
    }
}

@Composable
private fun ImageContent(modifier: Modifier, image: AnalyzingImage, onClick: () -> Unit) {
    val context = LocalContext.current
    AsyncImage(
        model = ImageRequest.Builder(context)
            .crossfade(true)
            .data(image.uri)
            .memoryCacheKey(image.cacheKey)
            .diskCacheKey(image.cacheKey)
            .build(),
        contentDescription = stringResource(id = R.string.image_being_analyzed),
        modifier = Modifier
            .then(modifier)
            .padding(4.dp)
            .fillMaxSize()
        //.clickable(onClick = onClick),
    )
}

@Composable
private fun AnalysisResultContent(
    modifier: Modifier = Modifier,
    analysisResult: AnalysisResult
) {
    when (analysisResult) {
        is AnalysisResult.Success -> {
            if (analysisResult.imageObject.isEmpty()) {
                AnalysisResultEmpty(modifier = modifier)
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .then(modifier)
                ) {
                    Text(
                        text = stringResource(id = R.string.image_analysis_result_title),
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)
                    )
                    AnalysisResultList(
                        modifier = modifier,
                        imageObjects = analysisResult.imageObject
                    )
                }
            }
        }
        AnalysisResult.Processing -> AnalysisLoading(modifier = modifier)
        AnalysisResult.ImageLoadFailed -> AnalysisImageLoadFailed(modifier = modifier)
        AnalysisResult.ModelLoadFailed -> AnalysisModelLoadFailed(modifier = modifier)
    }
}

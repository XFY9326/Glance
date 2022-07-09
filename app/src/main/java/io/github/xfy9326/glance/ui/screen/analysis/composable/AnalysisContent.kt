package io.github.xfy9326.glance.ui.screen.analysis.composable

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.xfy9326.atools.compose.common.DividedLayout
import io.github.xfy9326.glance.R
import io.github.xfy9326.glance.ui.common.AnalysisResultStatus
import io.github.xfy9326.glance.ui.common.PreviewImageObjectInfo
import io.github.xfy9326.glance.ui.common.SimpleTopAppToolBar
import io.github.xfy9326.glance.ui.data.AnalysisResult
import io.github.xfy9326.glance.ui.data.AnalyzingImage
import io.github.xfy9326.glance.ui.data.ImageObject
import io.github.xfy9326.glance.ui.theme.AppTheme

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
private fun PreviewAnalysisContent() {
    AppTheme {
        AnalysisScreenContent(
            scaffoldState = rememberScaffoldState(),
            image = AnalyzingImage(Uri.EMPTY),
            analysisResult = AnalysisResult.Success(
                imageObjectInfo = PreviewImageObjectInfo,
                caption = "test image caption"
            ),
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
                .fillMaxWidth(),
            weightUpStart = 0.45f,
            weightDownEnd = 0.55f,
            contentUpStart = {
                ImageContent(
                    modifier = Modifier.align(Alignment.Center),
                    image = image,
                    analysisResult = analysisResult
                )
            },
            contentDownEnd = {
                ResultContent(analysisResult = analysisResult)
            }
        )
    }
}

@Composable
private fun ImageContent(
    modifier: Modifier,
    image: AnalyzingImage,
    analysisResult: AnalysisResult
) {
    AnalysisImageView(
        modifier = modifier,
        image = image,
        analysisResult = analysisResult
    )
}

@Composable
private fun ResultContent(analysisResult: AnalysisResult) {
    AnalysisResultStatus(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .semantics {
                liveRegion = LiveRegionMode.Polite
            },
        analysisResult = analysisResult
    ) {
        AnalysisImageCaption(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            caption = it.caption
        )
        Divider()
        AnalysisImageObjects(
            modifier = Modifier.fillMaxWidth(),
            imageObjects = it.imageObjectInfo.objects
        )
    }
}

@Composable
private fun AnalysisImageObjects(
    modifier: Modifier,
    imageObjects: List<ImageObject>
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier
                .padding(start = 10.dp, top = 8.dp, bottom = 4.dp)
                .semantics { heading() },
            text = stringResource(id = R.string.detected_objects_title),
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
        AnalysisResultList(
            modifier = Modifier.fillMaxWidth(),
            imageObjects = imageObjects
        )
    }
}

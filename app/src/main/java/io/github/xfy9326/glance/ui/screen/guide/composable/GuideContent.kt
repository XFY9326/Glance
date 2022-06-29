package io.github.xfy9326.glance.ui.screen.guide.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.xfy9326.glance.R
import io.github.xfy9326.glance.ui.base.PreviewSurfaceProvider
import io.github.xfy9326.glance.ui.common.AnalysisResultContent
import io.github.xfy9326.glance.ui.common.DividedLayout
import io.github.xfy9326.glance.ui.common.PreviewImageObjectInfo
import io.github.xfy9326.glance.ui.common.SimpleTopAppToolBar
import io.github.xfy9326.glance.ui.data.AnalysisResult
import io.github.xfy9326.glance.ui.theme.AppTheme

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
private fun PreviewGuideContent() {
    AppTheme {
        GuideContent(
            scaffoldState = rememberScaffoldState(),
            onBackPressed = {},
            onBindCamera = {},
            analysisResult = AnalysisResult.Success(PreviewImageObjectInfo)
        )
    }
}

@Composable
fun GuideContent(
    scaffoldState: ScaffoldState,
    onBackPressed: () -> Unit,
    onBindCamera: (PreviewSurfaceProvider) -> Unit,
    analysisResult: AnalysisResult
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
        DividedLayout(
            modifier = Modifier
                .padding(it)
                .navigationBarsPadding()
                .fillMaxSize(),
            weightUpStart = 0.7f,
            weightDownEnd = 0.3f,
            contentUpStart = {
                GuideCameraPreview(
                    modifier = Modifier.fillMaxSize(),
                    analysisResult = analysisResult,
                    onBindCamera = onBindCamera
                )
            },
            contentDownEnd = {
                GuideResultContent(
                    modifier = Modifier.align(Alignment.Center),
                    analysisResult = analysisResult
                )
            }
        )
    }
}

@Composable
private fun GuideResultContent(
    modifier: Modifier,
    analysisResult: AnalysisResult
) {
    AnalysisResultContent(modifier = modifier, analysisResult = analysisResult) {
        Column(
            modifier = modifier.semantics(true) { liveRegion = LiveRegionMode.Polite },
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            it.take(3).forEach {
                Text(
                    text = it.classText,
                    fontSize = 26.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
package io.github.xfy9326.glance.ui.screen.finder.composable

import androidx.compose.foundation.focusable
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
import io.github.xfy9326.atools.compose.common.DividedLayout
import io.github.xfy9326.glance.R
import io.github.xfy9326.glance.ui.base.PreviewSurfaceProvider
import io.github.xfy9326.glance.ui.common.AnalysisResultContent
import io.github.xfy9326.glance.ui.common.PreviewImageObjectInfo
import io.github.xfy9326.glance.ui.common.SimpleTopAppToolBar
import io.github.xfy9326.glance.ui.data.AnalysisResult
import io.github.xfy9326.glance.ui.data.countOutputClasses
import io.github.xfy9326.glance.ui.theme.AppTheme

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
private fun PreviewFinderContent() {
    AppTheme {
        FinderContent(
            scaffoldState = rememberScaffoldState(),
            onBackPressed = {},
            onBindCamera = {},
            analysisResult = AnalysisResult.Success(
                PreviewImageObjectInfo.copy(objects = PreviewImageObjectInfo.objects.take(3))
            )
        )
    }
}

@Composable
fun FinderContent(
    scaffoldState: ScaffoldState,
    onBackPressed: () -> Unit,
    onBindCamera: (PreviewSurfaceProvider) -> Unit,
    analysisResult: AnalysisResult
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            SimpleTopAppToolBar(
                title = stringResource(id = R.string.finder),
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
                FinderCameraPreview(
                    modifier = Modifier.fillMaxSize(),
                    analysisResult = analysisResult,
                    onBindCamera = onBindCamera
                )
            },
            contentDownEnd = {
                ResultContent(
                    modifier = Modifier.align(Alignment.Center),
                    analysisResult = analysisResult
                )
            }
        )
    }
}

@Composable
private fun ResultContent(
    modifier: Modifier,
    analysisResult: AnalysisResult
) {
    AnalysisResultContent(
        modifier = modifier,
        analysisResult = analysisResult
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .focusable()
                .semantics(true) { liveRegion = LiveRegionMode.Assertive },
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            it.countOutputClasses().forEach {
                Text(
                    text = if (it.second > 1) "${it.second}  ${it.first}" else it.first,
                    fontSize = 26.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

package io.github.xfy9326.glance.ui.screen.scene.composable

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
import io.github.xfy9326.glance.ui.common.AnalysisResultStatus
import io.github.xfy9326.glance.ui.common.CameraPreview
import io.github.xfy9326.glance.ui.common.KeepScreenOn
import io.github.xfy9326.glance.ui.common.SimpleTopAppToolBar
import io.github.xfy9326.glance.ui.data.AnalysisResult
import io.github.xfy9326.glance.ui.theme.AppTheme

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
private fun PreviewSceneContent() {
    AppTheme {
        SceneContent(
            scaffoldState = rememberScaffoldState(),
            onBackPressed = {},
            onBindCamera = {},
            analysisResult = AnalysisResult.CaptionGenerateSuccess("Caption text")
        )
    }
}

@Composable
fun SceneContent(
    scaffoldState: ScaffoldState,
    onBackPressed: () -> Unit,
    onBindCamera: (PreviewSurfaceProvider) -> Unit,
    analysisResult: AnalysisResult
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            SimpleTopAppToolBar(
                title = stringResource(id = R.string.scene),
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
            modifierUpStart = Modifier.fillMaxWidth(),
            modifierDownEnd = Modifier.fillMaxWidth(),
            contentUpStart = {
                KeepScreenOn()
                CameraPreview(
                    modifier = Modifier.fillMaxSize(),
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
    AnalysisResultStatus(
        modifier = modifier,
        analysisResult = analysisResult,
        onCaptionGenerateSuccess = {
            Box(
                modifier = Modifier
                    .padding(6.dp)
                    .semantics(true) {
                        liveRegion = LiveRegionMode.Assertive
                    }
            ) {
                Text(
                    text = it.caption,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    )
}
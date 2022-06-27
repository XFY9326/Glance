package io.github.xfy9326.glance.ui.screen.guide.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.xfy9326.glance.R
import io.github.xfy9326.glance.ui.base.PreviewUseCase
import io.github.xfy9326.glance.ui.common.DividedLayout
import io.github.xfy9326.glance.ui.common.ImageObjectBoxLayer
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
    onBindCamera: (PreviewUseCase) -> Unit,
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
                    onBindCamera = onBindCamera
                )
                if (analysisResult is AnalysisResult.Success) {
                    ImageObjectBoxLayer(
                        imageObjectInfo = analysisResult.imageObjectInfo,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            },
            contentDownEnd = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (analysisResult is AnalysisResult.Success) {
                        analysisResult.imageObjectInfo.objects.forEach { obj ->
                            Text(text = "${obj.classText}   ${obj.reliability}%")
                        }
                    }
                }
            }
        )
    }
}

package io.github.xfy9326.glance.ui.screen.analysis.composable

import android.net.Uri
import androidx.compose.foundation.focusable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.xfy9326.glance.R
import io.github.xfy9326.glance.ui.common.AnalysisResultContent
import io.github.xfy9326.glance.ui.common.DividedLayout
import io.github.xfy9326.glance.ui.common.SimpleTopAppToolBar
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
            analysisResult = AnalysisResult.Initializing,
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
                .fillMaxSize(),
            modifierDownEnd = Modifier.semantics {
                liveRegion = LiveRegionMode.Polite
            },
            contentUpStart = {
                ImageContent(
                    modifier = Modifier.align(Alignment.Center),
                    image = image,
                    analysisResult = analysisResult
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
private fun ResultContent(
    modifier: Modifier,
    analysisResult: AnalysisResult
) {
    AnalysisResultContent(
        modifier = modifier,
        analysisResult = analysisResult
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .focusable()
        ) {
            Text(
                text = stringResource(id = R.string.image_analysis_result_title),
                modifier = Modifier
                    .padding(horizontal = 18.dp, vertical = 10.dp)
                    .semantics { heading() }
            )
            AnalysisResultList(
                modifier = Modifier.fillMaxSize(),
                imageObjects = it
            )
        }
    }
}

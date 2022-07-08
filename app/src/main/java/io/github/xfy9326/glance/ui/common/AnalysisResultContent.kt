package io.github.xfy9326.glance.ui.common

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.outlined.BrokenImage
import androidx.compose.material.icons.outlined.Dangerous
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.xfy9326.atools.compose.modifier.scrollBarStyle
import io.github.xfy9326.atools.compose.modifier.verticalScrollbar
import io.github.xfy9326.glance.R
import io.github.xfy9326.glance.ui.data.AnalysisResult
import io.github.xfy9326.glance.ui.data.ImageObject
import io.github.xfy9326.glance.ui.data.hasObjects
import io.github.xfy9326.glance.ui.screen.analysis.composable.AnalysisLoading
import io.github.xfy9326.glance.ui.theme.AppTheme

@Preview(showBackground = true)
@Composable
private fun PreviewAnalysisModelSuccess() {
    AppTheme {
        AnalysisModelSuccess(
            modifier = Modifier,
            analysisResult = AnalysisResult.Success(PreviewImageObjectInfo, null),
            captionContent = {},
            imageObjectsContent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewAnalysisModelLoadFailed() {
    AppTheme {
        AnalysisModelProcessFailed(modifier = Modifier)
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewAnalysisLabelsLoadFailed() {
    AppTheme {
        AnalysisLabelsLoadFailed(modifier = Modifier)
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewAnalysisImageLoadFailed() {
    AppTheme {
        AnalysisImageLoadFailed(modifier = Modifier)
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewAnalysisResultEmpty() {
    AppTheme {
        AnalysisResultEmpty(modifier = Modifier)
    }
}

@Composable
fun AnalysisResultContent(
    modifier: Modifier,
    analysisResult: AnalysisResult,
    captionContent: (@Composable BoxScope.(String?) -> Unit)? = null,
    imageObjectsContent: @Composable ColumnScope.(List<ImageObject>) -> Unit
) {
    when (analysisResult) {
        is AnalysisResult.Success -> AnalysisModelSuccess(
            modifier = modifier,
            analysisResult = analysisResult,
            captionContent = captionContent,
            imageObjectsContent = imageObjectsContent
        )
        AnalysisResult.Initializing -> AnalysisLoading(modifier = modifier)
        AnalysisResult.ImageLoadFailed -> AnalysisImageLoadFailed(modifier = modifier)
        AnalysisResult.ModelProcessFailed -> AnalysisModelProcessFailed(modifier = modifier)
        AnalysisResult.LabelsLoadFailed -> AnalysisLabelsLoadFailed(modifier = modifier)
    }
}

@Composable
private fun AnalysisModelSuccess(
    modifier: Modifier,
    analysisResult: AnalysisResult.Success,
    captionContent: (@Composable BoxScope.(String?) -> Unit)? = null,
    imageObjectsContent: @Composable ColumnScope.(List<ImageObject>) -> Unit
) {
    val imageObjectCompose: @Composable ColumnScope.(List<ImageObject>) -> Unit = {
        if (analysisResult.hasObjects()) {
            imageObjectsContent(it)
        } else {
            AnalysisResultEmpty(
                modifier = Modifier.fillMaxSize()
            )
        }
    }
    if (captionContent == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .focusable()
                .then(modifier)
        ) {
            imageObjectCompose(analysisResult.imageObjectInfo.objects)
        }
    } else {
        val captionScrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(modifier)
        ) {
            Box(
                modifier = Modifier
                    .weight(0.25f)
                    .fillMaxWidth()
                    .verticalScroll(captionScrollState)
                    .verticalScrollbar(captionScrollState, style = scrollBarStyle())
                    .padding(6.dp)
                    .focusable()
                    .semantics(true) { },
                contentAlignment = Alignment.Center
            ) {
                captionContent(analysisResult.caption)
            }
            Divider()
            Column(
                modifier = Modifier
                    .weight(0.75f)
                    .fillMaxWidth()
                    .focusable()
            ) {
                imageObjectCompose(analysisResult.imageObjectInfo.objects)
            }
        }
    }
}

@Composable
private fun AnalysisModelProcessFailed(modifier: Modifier) {
    AnalysisMessage(
        text = stringResource(id = R.string.image_process_failed),
        icon = Icons.Outlined.Dangerous,
        modifier = modifier
    )
}

@Composable
private fun AnalysisLabelsLoadFailed(modifier: Modifier) {
    AnalysisMessage(
        text = stringResource(id = R.string.labels_load_failed),
        icon = Icons.Outlined.Dangerous,
        modifier = modifier
    )
}

@Composable
private fun AnalysisImageLoadFailed(modifier: Modifier) {
    AnalysisMessage(
        text = stringResource(id = R.string.image_load_failed),
        icon = Icons.Outlined.BrokenImage,
        modifier = modifier
    )
}

@Composable
private fun AnalysisResultEmpty(modifier: Modifier) {
    AnalysisMessage(
        text = stringResource(id = R.string.empty_image_analysis_result),
        icon = Icons.Default.SearchOff,
        modifier = modifier
    )
}

@Composable
private fun AnalysisMessage(
    text: String,
    icon: ImageVector,
    modifier: Modifier,
) {
    Row(
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth(fraction = 0.9f)
            .padding(horizontal = 6.dp, vertical = 4.dp)
            .semantics(true) {
                liveRegion = LiveRegionMode.Polite
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(44.dp)
        )
        Text(
            text = text,
            fontSize = 18.sp
        )
    }
}
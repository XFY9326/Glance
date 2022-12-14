package io.github.xfy9326.glance.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
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
import io.github.xfy9326.glance.R
import io.github.xfy9326.glance.ui.data.AnalysisResult
import io.github.xfy9326.glance.ui.screen.analysis.composable.AnalysisLoading
import io.github.xfy9326.glance.ui.theme.AppTheme


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

@Composable
fun AnalysisResultStatus(
    modifier: Modifier = Modifier,
    analysisResult: AnalysisResult,
    onCaptionGenerateSuccess: (@Composable BoxScope.(AnalysisResult.CaptionGenerateSuccess) -> Unit)? = null,
    onDetectSuccess: (@Composable ColumnScope.(AnalysisResult.DetectSuccess) -> Unit)? = null
) {
    when (analysisResult) {
        is AnalysisResult.CaptionGenerateSuccess -> Box(modifier = modifier) {
            onCaptionGenerateSuccess?.invoke(this, analysisResult)
        }
        is AnalysisResult.DetectSuccess -> Column(modifier = modifier) {
            onDetectSuccess?.invoke(this, analysisResult)
        }
        AnalysisResult.Initializing -> Box(modifier = modifier) {
            AnalysisLoading(modifier = Modifier.align(Alignment.Center))
        }
        AnalysisResult.ImageLoadFailed -> Box(modifier = modifier) {
            AnalysisImageLoadFailed(modifier = Modifier.align(Alignment.Center))
        }
        AnalysisResult.ModelProcessFailed -> Box(modifier = modifier) {
            AnalysisModelProcessFailed(modifier = Modifier.align(Alignment.Center))
        }
        AnalysisResult.ResourcesLoadFailed -> Box(modifier = modifier) {
            AnalysisLabelsLoadFailed(modifier = Modifier.align(Alignment.Center))
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
fun AnalysisMessage(
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
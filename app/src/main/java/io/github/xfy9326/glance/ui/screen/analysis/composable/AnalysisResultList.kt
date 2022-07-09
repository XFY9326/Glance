package io.github.xfy9326.glance.ui.screen.analysis.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.xfy9326.glance.R
import io.github.xfy9326.glance.ui.common.AnalysisMessage
import io.github.xfy9326.glance.ui.common.PreviewImageObjectInfo
import io.github.xfy9326.glance.ui.data.ImageObject
import io.github.xfy9326.glance.ui.theme.AppTheme

@Preview(showBackground = true)
@Composable
private fun PreviewAnalysisResultList() {
    AppTheme {
        AnalysisResultList(
            modifier = Modifier,
            imageObjects = PreviewImageObjectInfo.objects
        )
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
fun AnalysisResultList(
    modifier: Modifier,
    imageObjects: List<ImageObject>
) {
    Box(modifier = modifier) {
        if (imageObjects.isEmpty()) {
            AnalysisResultEmpty(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 28.dp, bottom = 8.dp)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                imageObjects.forEach {
                    AnalysisResultItem(it)
                }
            }
        }
    }
}

@Composable
private fun AnalysisResultItem(
    imageObject: ImageObject
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 14.dp)
            .semantics(true) { },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = imageObject.classText.replaceFirstChar { it.uppercaseChar() },
            modifier = Modifier.weight(1f)
        )
        Text(
            text = stringResource(id = R.string.reliability, imageObject.reliability),
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End
        )
    }
}

@Composable
fun AnalysisResultEmpty(modifier: Modifier) {
    AnalysisMessage(
        text = stringResource(id = R.string.empty_image_analysis_result),
        icon = Icons.Default.SearchOff,
        modifier = modifier
    )
}
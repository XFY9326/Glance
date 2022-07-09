package io.github.xfy9326.glance.ui.screen.analysis.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.xfy9326.glance.R
import io.github.xfy9326.glance.ui.theme.AppTheme

@Preview(showBackground = true)
@Composable
private fun PreviewAnalysisImageCaption() {
    AppTheme {
        AnalysisImageCaption(
            modifier = Modifier,
            caption = "test image caption"
        )
    }
}

@Composable
fun AnalysisImageCaption(
    modifier: Modifier,
    caption: String?
) {
    val showText = if (caption.isNullOrEmpty()) {
        stringResource(id = R.string.image_no_description)
    } else {
        caption
    }
    Column(modifier = modifier) {
        Text(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .semantics { heading() },
            text = stringResource(id = R.string.image_caption_title),
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 84.dp)
                .padding(horizontal = 4.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = showText,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
            )
        }
    }
}
package io.github.xfy9326.glance.ui.screen.analysis.composable

import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.xfy9326.glance.R
import io.github.xfy9326.glance.ui.theme.AppTheme


@Preview(showBackground = true)
@Composable
private fun PreviewAnalysisModelLoadFailed() {
    AppTheme {
        AnalysisModelLoadFailed(modifier = Modifier)
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
fun AnalysisModelLoadFailed(modifier: Modifier) {
    AnalysisMessage(
        text = stringResource(id = R.string.model_init_failed),
        icon = Icons.Outlined.Dangerous,
        modifier = modifier
    )
}

@Composable
fun AnalysisImageLoadFailed(modifier: Modifier) {
    AnalysisMessage(
        text = stringResource(id = R.string.image_load_failed),
        icon = Icons.Outlined.BrokenImage,
        modifier = modifier
    )
}

@Composable
fun AnalysisResultEmpty(modifier: Modifier) {
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
            .semantics(true) { },
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
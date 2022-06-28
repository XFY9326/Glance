package io.github.xfy9326.glance.ui.screen.analysis.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.xfy9326.glance.R
import io.github.xfy9326.glance.ui.theme.AppTheme

@Preview(showBackground = true)
@Composable
private fun PreviewAnalysisLoading() {
    AppTheme {
        AnalysisLoading(modifier = Modifier)
    }
}

@Composable
fun AnalysisLoading(modifier: Modifier) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .semantics(true) {}
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterVertically)
    ) {
        CircularProgressIndicator()
        Text(
            text = stringResource(id = R.string.image_processing),
            fontSize = 18.sp
        )
    }
}
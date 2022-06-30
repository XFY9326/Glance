package io.github.xfy9326.glance.ui.screen.analysis.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.xfy9326.glance.R
import io.github.xfy9326.glance.ui.common.PreviewImageObjectInfo
import io.github.xfy9326.glance.ui.data.ImageObject
import io.github.xfy9326.glance.ui.theme.AppTheme
import io.github.xfy9326.glance.ui.theme.AppThemeShape

@Preview(showBackground = true)
@Composable
private fun PreviewAnalysisResultList() {
    AppTheme {
        AnalysisResultList(
            imageObjects = PreviewImageObjectInfo.objects
        )
    }
}

@Composable
fun AnalysisResultList(
    modifier: Modifier = Modifier,
    imageObjects: List<ImageObject>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .then(modifier)
    ) {
        items(imageObjects) {
            AnalysisResultItem(it)
        }
    }
}

@Composable
private fun AnalysisResultItem(
    imageObject: ImageObject
) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .fillMaxWidth()
            .semantics(true) { },
        shape = AppThemeShape.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
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
}
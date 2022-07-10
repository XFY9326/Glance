package io.github.xfy9326.glance.ui.screen.analysis.composable

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import io.github.xfy9326.glance.R
import io.github.xfy9326.glance.ui.common.ImageObjectBoxLayer
import io.github.xfy9326.glance.ui.common.PreviewImageObjectInfo
import io.github.xfy9326.glance.ui.data.AnalysisResult
import io.github.xfy9326.glance.ui.data.AnalyzingImage
import io.github.xfy9326.glance.ui.theme.AppTheme

@Preview(showBackground = true, widthDp = 500, heightDp = 500)
@Composable
private fun PreviewAnalysisImageView() {
    AppTheme {
        AnalysisImageView(
            modifier = Modifier.size(500.dp),
            image = AnalyzingImage(Uri.EMPTY),
            analysisResult = AnalysisResult.DetectSuccess(PreviewImageObjectInfo)
        )
    }
}

@Composable
fun AnalysisImageView(
    modifier: Modifier,
    image: AnalyzingImage,
    analysisResult: AnalysisResult
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .then(modifier)
            .fillMaxSize()
            .padding(4.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .crossfade(true)
                .data(image.uri)
                .memoryCacheKey(image.cacheKey)
                .diskCacheKey(image.cacheKey)
                .build(),
            contentDescription = stringResource(id = R.string.analyzed_image),
            modifier = Modifier.fillMaxSize(),
        )
        if (analysisResult is AnalysisResult.DetectSuccess) {
            ImageObjectBoxLayer(
                imageObjectInfo = analysisResult.imageObjectInfo,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
package io.github.xfy9326.glance.ui.screen.analysis.composable

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import io.github.xfy9326.glance.ui.data.AnalyzingImage
import io.github.xfy9326.glance.ui.data.ImageObject
import io.github.xfy9326.glance.ui.theme.AppTheme

@Preview(showBackground = true)
@Composable
private fun PreviewPictureTouchDialog() {
    AppTheme {
        PictureTouchDialog(
            image = AnalyzingImage(Uri.EMPTY),
            imageObjects = emptyList(),
            onDismissRequest = {}
        )
    }
}

@Composable
fun PictureTouchDialog(
    image: AnalyzingImage,
    imageObjects: List<ImageObject>,
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current
    Dialog(onDismissRequest = onDismissRequest) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(image.uri)
                .memoryCacheKey(image.cacheKey)
                .diskCacheKey(image.cacheKey)
                .build(),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}
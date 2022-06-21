package io.github.xfy9326.glance.ui.screen.analysis.composable

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import io.github.xfy9326.glance.R
import io.github.xfy9326.glance.ui.common.SimpleTopAppToolBar
import io.github.xfy9326.glance.ui.theme.AppTheme

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
private fun PreviewAnalysisContent() {
    AppTheme {
        AnalysisScreenContent(
            scaffoldState = rememberScaffoldState(),
            imageUri = Uri.EMPTY,
            analyzeText = "Loading ...",
            onBackPressed = {}
        )
    }
}

@Composable
fun AnalysisScreenContent(
    scaffoldState: ScaffoldState,
    imageUri: Uri,
    analyzeText: String,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            SimpleTopAppToolBar(
                title = stringResource(id = R.string.image_analysis),
                onBackPressed = onBackPressed
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .navigationBarsPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .crossfade(true)
                    .memoryCachePolicy(CachePolicy.DISABLED)
                    .data(imageUri)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(fraction = 0.8f)
            )
            Text(text = analyzeText, modifier = Modifier.padding(5.dp))
        }
    }
}
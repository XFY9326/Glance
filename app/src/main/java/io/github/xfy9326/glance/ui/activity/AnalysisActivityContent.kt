package io.github.xfy9326.glance.ui.activity

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import io.github.xfy9326.glance.ui.theme.AppTheme

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun AnalysisActivityContent() {
    AppTheme {
        Scaffold {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {

            }
        }
    }
}
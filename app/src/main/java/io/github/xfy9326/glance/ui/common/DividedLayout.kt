package io.github.xfy9326.glance.ui.common

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DividedLayout(
    modifier: Modifier,
    weightUpStart: Float = 0.5f,
    weightDownEnd: Float = 0.5f,
    middleSpaceSize: Dp = 0.dp,
    contentUpStart: @Composable BoxScope.() -> Unit,
    contentDownEnd: @Composable BoxScope.() -> Unit
) {
    val configuration = LocalConfiguration.current
    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        Row(modifier = modifier) {
            Box(
                modifier = Modifier
                    .weight(weightUpStart)
                    .fillMaxSize(),
                content = contentUpStart
            )
            if (middleSpaceSize.value > 0) {
                Spacer(modifier = Modifier.height(middleSpaceSize))
            }
            Box(
                modifier = Modifier
                    .weight(weightDownEnd)
                    .fillMaxSize(),
                content = contentDownEnd
            )
        }
    } else {
        Column(modifier = modifier) {
            Box(
                modifier = Modifier
                    .weight(weightUpStart)
                    .fillMaxSize(),
                content = contentUpStart
            )
            if (middleSpaceSize.value > 0) {
                Spacer(modifier = Modifier.height(middleSpaceSize))
            }
            Box(
                modifier = Modifier
                    .weight(weightDownEnd)
                    .fillMaxSize(),
                content = contentDownEnd
            )
        }
    }
}
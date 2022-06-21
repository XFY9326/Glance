package io.github.xfy9326.glance.ui.screen.analysis.composable

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import io.github.xfy9326.glance.R
import io.github.xfy9326.glance.ui.common.TopAppToolBar

@Composable
fun AnalysisTopAppBar(
    title: String,
    onBackPressed: () -> Unit
) {
    TopAppToolBar(
        title = {
            Text(text = title)
        },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.back)
                )
            }
        }
    )
}
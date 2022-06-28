package io.github.xfy9326.glance.ui.screen.main.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.xfy9326.glance.ui.theme.AppTheme

@Preview
@Composable
private fun PreviewFunctionCard() {
    AppTheme {
        FunctionCard(
            icon = Icons.Default.Android,
            title = "Title",
            summary = "Summary",
            onClick = {}
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FunctionCard(icon: ImageVector, title: String, summary: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(84.dp)
            .semantics(mergeDescendants = true) {}
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 18.dp, vertical = 4.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(36.dp)
            )
            Column(
                modifier = Modifier.padding(start = 12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = title, fontSize = 16.sp)
                Text(text = summary, fontSize = 14.sp)
            }
        }
    }
}
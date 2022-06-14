package io.github.xfy9326.glance.ui.base

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FunctionCard(icon: ImageVector, title: String, summary: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(84.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 18.dp, vertical = 4.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(36.dp)
            )
            Column(
                modifier = Modifier.padding(start = 15.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = title, fontSize = 16.sp)
                Text(text = summary, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun FunctionItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier.size(30.dp)
        )
        Text(
            text = title,
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 20.dp)
        )
    }
}
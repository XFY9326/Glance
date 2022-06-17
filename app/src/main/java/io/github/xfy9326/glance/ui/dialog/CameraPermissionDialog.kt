package io.github.xfy9326.glance.ui.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.xfy9326.glance.R

@Composable
fun createCameraPermissionDialog(
    onRequestPermission: () -> Unit
): DialogController {
    val state = remember { mutableStateOf(false) }
    if (state.value) {
        AlertDialog(
            onDismissRequest = { state.value = false },
            title = {
                Text(text = stringResource(id = R.string.permission_request))
            },
            text = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                    Column(
                        modifier = Modifier
                            .padding(start = 18.dp)
                            .semantics(mergeDescendants = true) { },
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(text = stringResource(id = R.string.camera_permission), fontWeight = FontWeight.Medium)
                        Text(text = stringResource(id = R.string.camera_permission_summary))
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    state.value = false
                    onRequestPermission()
                }) {
                    Text(text = stringResource(id = android.R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { state.value = false }) {
                    Text(text = stringResource(id = android.R.string.cancel))
                }
            }
        )
    }
    return DialogController(state)
}

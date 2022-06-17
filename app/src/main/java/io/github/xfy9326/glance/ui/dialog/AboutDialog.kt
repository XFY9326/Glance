package io.github.xfy9326.glance.ui.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import io.github.xfy9326.glance.BuildConfig
import io.github.xfy9326.glance.R
import io.github.xfy9326.glance.ui.base.ApplicationIcon

@Composable
fun createAboutDialog(): DialogController {
    val state = remember { mutableStateOf(false) }
    if (state.value) {
        Dialog(onDismissRequest = { state.value = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ApplicationIcon(modifier = Modifier.size(80.dp))
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .fillMaxHeight()
                            .semantics(mergeDescendants = true) {},
                        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically)
                    ) {
                        Text(text = stringResource(id = R.string.app_name), fontSize = 16.sp)
                        Text(text = BuildConfig.VERSION_NAME, fontSize = 14.sp)
                    }
                }
            }
        }
    }
    return DialogController(state)
}

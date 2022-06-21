package io.github.xfy9326.glance.ui.base

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

@Composable
fun rememberLauncherForRequestPermission(onResult: (Boolean) -> Unit) =
    rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission(), onResult)

@Composable
fun rememberLauncherForTakePicture(onResult: (Boolean) -> Unit) =
    rememberLauncherForActivityResult(ActivityResultContracts.TakePicture(), onResult)

@Composable
fun rememberLauncherForGetContent(onResult: (Uri?) -> Unit) =
    rememberLauncherForActivityResult(ActivityResultContracts.GetContent(), onResult)
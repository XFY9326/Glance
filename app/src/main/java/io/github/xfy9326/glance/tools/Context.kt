package io.github.xfy9326.glance.tools

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

inline fun <reified A : Activity> Context.startActivity(intentBlock: Intent.() -> Unit = {}) {
    startActivity(Intent(this, A::class.java).apply(intentBlock))
}

fun Context.isPermissionGranted(permission: String): Boolean =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
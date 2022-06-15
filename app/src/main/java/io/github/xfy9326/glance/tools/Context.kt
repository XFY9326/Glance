package io.github.xfy9326.glance.tools

import android.app.Activity
import android.content.Context
import android.content.Intent

inline fun <reified A : Activity> Context.startActivity(intentBlock: Intent.() -> Unit = {}) {
    startActivity(Intent(this, A::class.java).apply(intentBlock))
}
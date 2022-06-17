@file:Suppress("unused")

package io.github.xfy9326.glance.ui.dialog

import androidx.compose.runtime.MutableState

@JvmInline
value class DialogController(private val state: MutableState<Boolean>) {
    fun show() {
        state.value = true
    }

    fun dismiss() {
        state.value = false
    }

    val isShowing: Boolean
        get() = state.value
}
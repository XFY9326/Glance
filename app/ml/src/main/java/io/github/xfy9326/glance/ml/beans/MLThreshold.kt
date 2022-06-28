@file:Suppress("unused")

package io.github.xfy9326.glance.ml.beans

sealed class MLThreshold(internal val value: Float) {
    sealed class Confidence(value: Float) : MLThreshold(value) {
        object Low : Confidence(0.25f)
        object Medium : Confidence(0.45f)
        object High : Confidence(0.65f)
    }

    sealed class IOU(value: Float) : MLThreshold(value) {
        object Low : IOU(0.25f)
        object Medium : IOU(0.45f)
        object High : IOU(0.65f)
    }
}
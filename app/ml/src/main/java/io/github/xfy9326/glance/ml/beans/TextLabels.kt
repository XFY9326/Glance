package io.github.xfy9326.glance.ml.beans

@JvmInline
value class TextLabels internal constructor(private val labels: Array<String>) {
    val size: Int
        get() = labels.size

    operator fun get(id: Int): String = labels[id]
}
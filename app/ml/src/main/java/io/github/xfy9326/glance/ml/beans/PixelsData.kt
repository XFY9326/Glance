package io.github.xfy9326.glance.ml.beans

import androidx.annotation.Keep
import java.nio.ByteBuffer

@Keep
data class PixelsData(
    val width: Int,
    val height: Int,
    val stride: Int,
    val pixels: ByteBuffer
)

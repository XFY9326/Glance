package io.github.xfy9326.glance.ml.beans

import java.nio.ByteBuffer

data class PixelsData(
    val width: Int,
    val height: Int,
    val stride: Int,
    val pixels: ByteBuffer
)

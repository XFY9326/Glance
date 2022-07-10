@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package io.github.xfy9326.glance.ml

import android.graphics.Bitmap
import android.util.Log
import io.github.xfy9326.atools.io.IOManager
import io.github.xfy9326.atools.io.file.assetFile
import io.github.xfy9326.atools.io.okio.readAllLines
import io.github.xfy9326.atools.io.okio.source
import io.github.xfy9326.atools.io.okio.useBuffer
import io.github.xfy9326.glance.ml.beans.ImageInfo
import io.github.xfy9326.glance.ml.beans.MLDetectOutput
import io.github.xfy9326.glance.ml.beans.PixelsData
import io.github.xfy9326.glance.ml.beans.TextLabels
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

object MLManager {
    private const val LABEL_CLASSES = "labels/classes.txt"
    private const val LABEL_VOCAB = "labels/vocab.txt"
    private const val VOCABULARY_META_SIZE = 3

    private val initMutex = Mutex()
    private val analyzeMutex = Mutex()
    private var isModelInitializeSuccess: Boolean? = null

    @OptIn(DelicateCoroutinesApi::class)
    fun initModelsInBackground() {
        GlobalScope.launch(Dispatchers.IO) {
            internalInit()
        }
    }

    fun isGPUInstanceCreated(): Boolean = NativeInterface.isGPUInstanceCreated()

    fun hasGPUSupport(): Boolean = NativeInterface.hasGPUSupport()

    fun isModelsInitialized(): Boolean = NativeInterface.isModelsInitialized()

    private suspend fun loadLabels(path: String): Result<TextLabels> = withContext(Dispatchers.IO) {
        runCatching {
            TextLabels(
                assetFile(path).source().useBuffer {
                    readAllLines().toTypedArray()
                }
            )
        }
    }

    suspend fun loadClasses(): Result<TextLabels> = loadLabels(LABEL_CLASSES)

    suspend fun loadVocabulary(): Result<TextLabels> = loadLabels(LABEL_VOCAB)

    private suspend fun internalInit(): Boolean {
        return isModelInitializeSuccess ?: initMutex.withLock {
            isModelInitializeSuccess ?: if (isModelsInitialized()) {
                isModelInitializeSuccess = true
                true
            } else {
                NativeInterface.initModels(IOManager.assetManager).also {
                    isModelInitializeSuccess = it
                }
            }
        }
    }

    private suspend fun <T, R> analyzeImage(onAnalyze: () -> T, onTransform: (T) -> R): Result<R> =
        withContext(Dispatchers.Default) {
            runCatching {
                if (internalInit()) {
                    onTransform(
                        analyzeMutex.withLock {
                            onAnalyze()
                        }
                    )
                } else {
                    error("Model init failed!")
                }
            }
        }

    private suspend fun detectImage(width: Int, height: Int, requestCaption: Boolean, block: () -> MLDetectOutput): Result<ImageInfo> =
        analyzeImage(
            onAnalyze = {
                block()
            },
            onTransform = {
                if (it.objects != null) {
                    if (!requestCaption || it.captionIds != null) {
                        return@analyzeImage ImageInfo(
                            width = width,
                            height = height,
                            objects = it.objects,
                            captionIds = it.captionIds
                        )
                    } else {
                        error("Caption generate failed!")
                    }
                } else {
                    error("Object detect failed!")
                }
            }
        )

    private fun <R> benchmark(type: String, block: () -> R): R {
        return if (BuildConfig.DEBUG) {
            val startMills = System.currentTimeMillis()
            block().also {
                Log.d("MLManager", "$type: ${System.currentTimeMillis() - startMills} mills")
            }
        } else {
            block()
        }
    }

    suspend fun analyzeImageCaptionByPixelsData(pixelsData: PixelsData): Result<IntArray> =
        analyzeImage(
            onAnalyze = {
                benchmark("analyzeImageCaptionByPixelsData") {
                    NativeInterface.analyzeImageCaptionByPixelsData(pixelsData)
                }
            },
            onTransform = {
                it ?: error("Caption generate failed!")
            }
        )

    suspend fun analyzeImageByPixelsData(
        data: PixelsData, confThreshold: Float, iouThreshold: Float, requestCaption: Boolean
    ): Result<ImageInfo> = detectImage(data.width, data.height, requestCaption) {
        benchmark("analyzeImageByPixelsData") {
            NativeInterface.analyzeImageByPixelsData(data, confThreshold, iouThreshold, requestCaption)
        }
    }

    suspend fun analyzeImageByBitmap(
        data: Bitmap, confThreshold: Float, iouThreshold: Float, requestCaption: Boolean
    ): Result<ImageInfo> = detectImage(data.width, data.height, requestCaption) {
        benchmark("analyzeImageByBitmap") {
            NativeInterface.analyzeImageByBitmap(data, confThreshold, iouThreshold, requestCaption)
        }
    }

    fun parseCaptionIds(captionIds: IntArray, vocabulary: TextLabels): String =
        captionIds.asSequence().mapNotNull {
            if (it >= VOCABULARY_META_SIZE && it < vocabulary.size) {
                vocabulary[it]
            } else {
                null
            }
        }.joinToString(" ")
}
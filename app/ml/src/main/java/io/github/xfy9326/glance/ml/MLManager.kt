@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package io.github.xfy9326.glance.ml

import io.github.xfy9326.atools.io.file.rawResFile
import io.github.xfy9326.atools.io.okio.source
import io.github.xfy9326.atools.io.okio.useBuffer
import io.github.xfy9326.glance.ml.beans.ModelType
import io.github.xfy9326.glance.ml.model.GeneralModel
import io.github.xfy9326.glance.ml.model.GuideModel
import io.github.xfy9326.glance.ml.model.Model
import kotlinx.coroutines.*
import org.json.JSONArray

object MLManager {
    private var hasInstanceCreated = false

    fun initInstance() {
        if (!hasInstanceCreated) {
            hasInstanceCreated = true
            NativeInterface
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun initModelsInBackground() {
        GlobalScope.launch(Dispatchers.IO) {
            ModelType.values().forEach {
                getModel(it).init()
            }
        }
    }

    fun isGPUInstanceCreated(): Boolean =
        NativeInterface.isGPUInstanceCreated()

    fun hasGPUSupport(): Boolean =
        NativeInterface.hasGPUSupport()

    suspend fun loadGuideLabels(modelType: ModelType): Array<String> = withContext(Dispatchers.IO) {
        rawResFile(modelType.labelsResId).source().useBuffer {
            JSONArray(readUtf8()).let { json ->
                Array(json.length()) {
                    json.optString(it)
                }
            }
        }
    }

    fun getModel(modelType: ModelType): Model =
        when (modelType) {
            ModelType.GUIDE_MODEL -> GuideModel
            ModelType.GENERAL_MODEL -> GeneralModel
        }
}
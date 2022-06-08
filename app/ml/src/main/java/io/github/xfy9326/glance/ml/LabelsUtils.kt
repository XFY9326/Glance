package io.github.xfy9326.glance.ml

import androidx.annotation.RawRes
import io.github.xfy9326.atools.io.file.rawResFile
import io.github.xfy9326.atools.io.okio.source
import io.github.xfy9326.atools.io.okio.useBuffer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray

internal object LabelsUtils {
    suspend fun loadLabels(@RawRes resId: Int): Array<String> = withContext(Dispatchers.IO) {
        try {
            rawResFile(resId).source().useBuffer {
                JSONArray(readUtf8()).let { json ->
                    Array(json.length()) {
                        json.optString(it)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            error("Failed to load labels from raw resources '$resId'!")
        }
    }
}
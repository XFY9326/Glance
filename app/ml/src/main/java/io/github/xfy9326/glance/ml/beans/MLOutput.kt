package io.github.xfy9326.glance.ml.beans

import androidx.annotation.Keep

@Keep
internal data class MLOutput internal constructor(
    val objects: Array<DetectObject>?,
    val captionIds: IntArray?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MLOutput

        if (objects != null) {
            if (other.objects == null) return false
            if (!objects.contentEquals(other.objects)) return false
        } else if (other.objects != null) return false
        if (captionIds != null) {
            if (other.captionIds == null) return false
            if (!captionIds.contentEquals(other.captionIds)) return false
        } else if (other.captionIds != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = objects?.contentHashCode() ?: 0
        result = 31 * result + (captionIds?.contentHashCode() ?: 0)
        return result
    }
}
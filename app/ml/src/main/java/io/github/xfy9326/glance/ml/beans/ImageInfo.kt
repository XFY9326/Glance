package io.github.xfy9326.glance.ml.beans

data class ImageInfo internal constructor(
    val width: Int,
    val height: Int,
    val objects: Array<DetectObject>,
    val captionIds: IntArray?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImageInfo

        if (width != other.width) return false
        if (height != other.height) return false
        if (!objects.contentEquals(other.objects)) return false
        if (captionIds != null) {
            if (other.captionIds == null) return false
            if (!captionIds.contentEquals(other.captionIds)) return false
        } else if (other.captionIds != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = width
        result = 31 * result + height
        result = 31 * result + objects.contentHashCode()
        result = 31 * result + (captionIds?.contentHashCode() ?: 0)
        return result
    }
}

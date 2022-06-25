package io.github.xfy9326.glance.ml.beans

data class DetectInfo(
    val width: Int,
    val height: Int,
    val objects: Array<DetectObject>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DetectInfo

        if (width != other.width) return false
        if (height != other.height) return false
        if (!objects.contentEquals(other.objects)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = width
        result = 31 * result + height
        result = 31 * result + objects.contentHashCode()
        return result
    }
}

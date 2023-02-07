package com.driff.android.module.domain.model.entity

data class PictureOfDay(
    val date: String,
    val mediaType: String,
    val title: String,
    val description: String,
    val imageByteArray: ByteArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PictureOfDay

        if (date != other.date) return false
        if (mediaType != other.mediaType) return false
        if (title != other.title) return false
        if (description != other.description) return false

        return true
    }

    override fun hashCode(): Int {
        var result = date.hashCode()
        result = 31 * result + mediaType.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        return result
    }
}

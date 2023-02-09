package com.driff.android.module.data.model

import com.driff.android.module.domain.entity.PictureOfDayEntity

data class NasaPictureModel(
    val date: String,
    val mediaType: String,
    val hdurl: String,
    val explanation: String,
    val title: String,
    val url: String
)


fun NasaPictureModel.toEntity(): PictureOfDayEntity = PictureOfDayEntity(
    date = date,
    mediaType = mediaType,
    title = title,
    description = explanation,
    url = url,
    hdUrl = hdurl
)

fun PictureOfDayEntity.toModel(): NasaPictureModel = NasaPictureModel(
    date = date,
    mediaType = mediaType,
    title = title,
    explanation = description,
    url = url,
    hdurl = hdUrl
)
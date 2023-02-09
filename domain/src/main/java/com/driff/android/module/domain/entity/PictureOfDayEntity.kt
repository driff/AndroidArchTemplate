package com.driff.android.module.domain.entity

data class PictureOfDayEntity(
    val date: String,
    val mediaType: String,
    val title: String,
    val description: String,
    val url: String,
    val hdUrl: String,
)

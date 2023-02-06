package com.driff.android.module.domain.model.entity

data class PictureOfDay(
    val date: String,
    val mediaType: String,
    val title: String,
    val description: String,
    val url: String,
    val hdUrl: String,
)

package com.driff.android.module.data.repository

import com.driff.android.module.data.entities.app.PictureOfDay

interface NasaPicturesRepository {

    suspend fun fetchNasaPictureOfTheDay(refresh: Boolean): List<PictureOfDay>

}
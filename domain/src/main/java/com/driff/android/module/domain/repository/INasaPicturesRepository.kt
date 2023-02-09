package com.driff.android.module.domain.repository

import com.driff.android.module.domain.entity.PictureOfDayEntity

interface INasaPicturesRepository {
    suspend fun fetchNasaPictureOfTheDay(refresh: Boolean = false): Result<PictureOfDayEntity>
}
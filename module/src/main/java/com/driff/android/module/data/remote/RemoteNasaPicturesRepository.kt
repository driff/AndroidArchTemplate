package com.driff.android.module.data.remote

import com.driff.android.module.data.datasource.NasaPicturesRemoteDataSource
import com.driff.android.module.data.entities.app.PictureOfDay
import com.driff.android.module.data.repository.NasaPicturesRepository
import kotlinx.coroutines.sync.Mutex

class RemoteNasaPicturesRepository constructor(
    private val remoteNasaPicturesDataSource: NasaPicturesRemoteDataSource
    ): NasaPicturesRepository {

    private val picturesMutex = Mutex()
    private var picturesOfDay: List<PictureOfDay> = listOf()

    override suspend fun fetchNasaPictureOfTheDay(refresh: Boolean): List<PictureOfDay> {
        return listOf()
    }
}
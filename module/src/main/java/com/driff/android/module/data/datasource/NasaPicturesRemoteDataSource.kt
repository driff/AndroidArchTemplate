package com.driff.android.module.data.datasource

import com.driff.android.module.data.api.NasaPicturesApi
import com.driff.android.module.data.remote.model.RemotePictureOfDay
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class NasaPicturesRemoteDataSource constructor(
    private val nasaPicturesApi: NasaPicturesApi,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun fetchPictureOfTheDay(): RemotePictureOfDay =
        withContext(ioDispatcher) {
            nasaPicturesApi.fetchPictureOfTheDay()
        }

    suspend fun fetchPictureOfTheDay(from: String, to: String): List<RemotePictureOfDay> =
        withContext(ioDispatcher) {
            nasaPicturesApi.fetchPicturesFromRange(from, to)
        }

}
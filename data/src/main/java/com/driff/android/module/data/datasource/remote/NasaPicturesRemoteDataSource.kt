package com.driff.android.module.data.datasource.remote

import com.driff.android.module.data.api.NasaPicturesApi
import com.driff.android.module.data.model.remote.RemoteNasaPicture
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class NasaPicturesRemoteDataSource constructor(
    private val nasaPicturesApi: NasaPicturesApi,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun fetchPictureOfTheDay(): Result<RemoteNasaPicture> =
        withContext(ioDispatcher) {
            try {
                val result = nasaPicturesApi.fetchPictureOfTheDay()
                return@withContext Result.success(result)
            } catch (e: Exception) {
                e.takeUnless { e is CancellationException }?.let {
                    return@withContext Result.failure(e)
                }?: throw e
            }
        }

    suspend fun fetchPicturesFromDate(from: String, to: String): List<RemoteNasaPicture> =
        withContext(ioDispatcher) {
            nasaPicturesApi.fetchPicturesFromRange(from, to)
        }

}
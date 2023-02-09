package com.driff.android.module.data.datasource.remote

import com.driff.android.module.data.api.NasaPicturesApi
import com.driff.android.module.data.model.NasaPictureModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class NasaPicturesRemoteDataSource constructor(
    private val nasaPicturesApi: NasaPicturesApi,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun fetchPictureOfTheDay(): Result<NasaPictureModel> =
        withContext(ioDispatcher) {
            try {
                val result = nasaPicturesApi.fetchPictureOfTheDay()
                return@withContext Result.success(result.toModel())
            } catch (e: Exception) {
                e.takeUnless { e is CancellationException }?.let {
                    return@withContext Result.failure(e)
                }?: throw e
            }
        }

    suspend fun fetchPicturesFromDate(from: String, to: String): List<NasaPictureModel> =
        withContext(ioDispatcher) {
            nasaPicturesApi.fetchPicturesFromRange(from, to).toListModel()
        }

}
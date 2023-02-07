package com.driff.android.module.data.datasource.remote

import com.driff.android.module.data.api.ImageLoaderApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ImageRemoteDataSource constructor(
    private val imageApi: ImageLoaderApi,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun fetchImage(url: String): Result<ByteArray> =
        withContext(ioDispatcher) {
            try {
                val imageAsByteArray = imageApi.fetchImage(url)
                return@withContext Result.success(imageAsByteArray)
            } catch (e: Exception) {
                return@withContext Result.failure(e)
            }
        }

}
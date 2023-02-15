package com.driff.android.module.data.datasource.remote

import android.util.Log
import com.driff.android.module.data.api.NasaPicturesApi
import com.driff.android.module.data.model.remote.RemoteNasaPicture
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.GlobalScope.coroutineContext
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext

class NasaPicturesRemoteDataSource constructor(
    private val nasaPicturesApi: NasaPicturesApi
) {

    suspend fun fetchPictureOfTheDay(): Result<RemoteNasaPicture> {
            Log.d(this@NasaPicturesRemoteDataSource::class.simpleName, "Coroutine name: ${coroutineContext.isActive}")
            try {
                val result = nasaPicturesApi.fetchPictureOfTheDay()
                return Result.success(result)
            } catch (e: Exception) {
                Log.e(this@NasaPicturesRemoteDataSource::class.simpleName, "Coroutine error: ${coroutineContext.isActive} - ${e.cause} ${e.message}")
                e.takeUnless { e is CancellationException }?.let {
                    return Result.failure(e)
                }?: throw e
            }
        }

    suspend fun fetchPicturesFromDate(from: String, to: String): List<RemoteNasaPicture> =
            nasaPicturesApi.fetchPicturesFromRange(from, to)

}
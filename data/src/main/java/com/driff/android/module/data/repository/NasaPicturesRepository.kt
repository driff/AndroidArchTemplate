package com.driff.android.module.data.repository

import android.util.Log
import com.driff.android.module.data.datasource.remote.NasaPicturesRemoteDataSource
import com.driff.android.module.data.model.entity.NasaPicture
import com.driff.android.module.data.model.mappers.asExternalModel
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class NasaPicturesRepository constructor (
    private val remoteNasaPicturesDataSource: NasaPicturesRemoteDataSource
) {
    private val picturesMutex = Mutex()
    private var picture: NasaPicture? = null

    suspend fun fetchNasaPictureOfTheDay(refresh: Boolean = false): Result<NasaPicture> {
        return try {
            picturesMutex.withLock {
            picture?.takeUnless { refresh }
                    ?.let { Result.success(it) }
            ?: coroutineScope {
                Log.d(this@NasaPicturesRepository::class.simpleName, "Coroutine isActive: ${coroutineContext.isActive}")
                Log.d(this@NasaPicturesRepository::class.simpleName, "scope is active: ${coroutineContext.isActive}")
                remoteNasaPicturesDataSource.fetchPictureOfTheDay()
                    .map { it.asExternalModel() }
                    .onSuccess {
                        picture = it
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(this@NasaPicturesRepository::class.simpleName, e.cause.toString())
            Log.e(this@NasaPicturesRepository::class.simpleName, e.message.orEmpty())
            return Result.failure(e)
        }
    }
}

package com.driff.android.module.data.repository

import com.driff.android.module.data.datasource.remote.NasaPicturesRemoteDataSource
import com.driff.android.module.data.model.entity.NasaPicture
import com.driff.android.module.data.utils.asExternalModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class NasaPicturesRepository constructor (
    private val remoteNasaPicturesDataSource: NasaPicturesRemoteDataSource,
    private val externalScope: CoroutineScope
) {
    private val picturesMutex = Mutex()
    private var picture: NasaPicture? = null

    suspend fun fetchNasaPictureOfTheDay(refresh: Boolean = false): Result<NasaPicture> {
        return try {
            picturesMutex.withLock {
            picture?.takeUnless { refresh }
                    ?.let { Result.success(it) }
            ?: withContext(externalScope.coroutineContext) {
                remoteNasaPicturesDataSource.fetchPictureOfTheDay()
                    .map { it.asExternalModel() }
                    .onSuccess {
                        picture = it
                    }
                }
            }
        } catch (e: Exception) {
                return Result.failure(e)
        }
    }
}

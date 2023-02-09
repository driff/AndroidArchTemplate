package com.driff.android.module.data.repository

import com.driff.android.module.data.datasource.remote.NasaPicturesRemoteDataSource
import com.driff.android.module.data.model.NasaPictureModel
import com.driff.android.module.data.model.toEntity
import com.driff.android.module.data.model.toModel
import com.driff.android.module.domain.entity.PictureOfDayEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class NasaPicturesRepository constructor (
    private val remoteNasaPicturesDataSource: NasaPicturesRemoteDataSource,
    private val externalScope: CoroutineScope
) {
    private val picturesMutex = Mutex()
    private var picture: NasaPictureModel? = null

    suspend fun fetchNasaPictureOfTheDay(refresh: Boolean = false): Result<PictureOfDayEntity> {
        return try {
            picturesMutex.withLock {
            picture?.takeUnless { refresh }
                    ?.let { Result.success(it.toEntity()) }
            ?: withContext(externalScope.coroutineContext) {
                remoteNasaPicturesDataSource.fetchPictureOfTheDay()
                    .map { it.toEntity() }
                    .onSuccess {
                        picture = it.toModel()
                    }
                }
            }
        } catch (e: Exception) {
                return Result.failure(e)
        }
    }
}

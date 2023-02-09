package com.driff.android.module.domain.usecase

import com.driff.android.module.domain.entity.PictureOfDayEntity
import com.driff.android.module.domain.repository.INasaPicturesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetNasaPictureOfDayUseCase(
    private val repository: INasaPicturesRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend operator fun invoke(refresh: Boolean = false): Result<PictureOfDayEntity> =
        withContext(defaultDispatcher) {
            repository.fetchNasaPictureOfTheDay(refresh)
        }
}
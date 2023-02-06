package com.driff.android.module.domain.interactor

import com.driff.android.module.data.model.entity.NasaPicture
import com.driff.android.module.data.repository.NasaPicturesRepository
import com.driff.android.module.domain.model.asExternalModel
import com.driff.android.module.domain.model.entity.PictureOfDay
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.TimeZone
import java.util.Timer

class GetNasaPictureOfDayUseCase(
    private val repository: NasaPicturesRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend operator fun invoke(refresh: Boolean = false): Result<PictureOfDay> =
        withContext(defaultDispatcher) {
            repository.fetchNasaPictureOfTheDay(refresh)
                .map(NasaPicture::asExternalModel)
        }
}
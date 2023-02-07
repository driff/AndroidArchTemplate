package com.driff.android.module.domain.interactor

import android.graphics.Bitmap
import com.driff.android.module.data.model.entity.NasaPicture
import com.driff.android.module.data.repository.ImageLoaderRepository
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
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val repository: NasaPicturesRepository,
    private val imageLoaderRepository: ImageLoaderRepository
) {
    suspend operator fun invoke(refresh: Boolean = false): Result<PictureOfDay> =
        withContext(defaultDispatcher) {
            repository.fetchNasaPictureOfTheDay(refresh)
                .mapCatching { it.asExternalModel(getImageFromUrl(it.url)) }
        }

    private suspend fun getImageFromUrl(url: String): ByteArray? = withContext(defaultDispatcher) {
        imageLoaderRepository.getImage(url).getOrNull()
    }
}
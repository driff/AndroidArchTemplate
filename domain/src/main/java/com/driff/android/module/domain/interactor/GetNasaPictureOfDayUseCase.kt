package com.driff.android.module.domain.interactor

import com.driff.android.module.data.repository.ImageLoaderRepository
import com.driff.android.module.data.repository.NasaPicturesRepository
import com.driff.android.module.domain.model.asExternalModel
import com.driff.android.module.domain.model.entity.PictureOfDay
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetNasaPictureOfDayUseCase(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val repository: NasaPicturesRepository,
    private val imageLoaderRepository: ImageLoaderRepository
) {
    suspend operator fun invoke(refresh: Boolean = false): Result<PictureOfDay> =
        withContext(defaultDispatcher) {
            repository.fetchNasaPictureOfTheDay(refresh)
                .mapCatching { response ->
                    response.asExternalModel().copy(imageByteArray = getImageFromUrl(response.url))
                }
        }

    private suspend fun getImageFromUrl(url: String): ByteArray? = withContext(defaultDispatcher) {
        imageLoaderRepository.getImage(url).getOrNull()
    }
}
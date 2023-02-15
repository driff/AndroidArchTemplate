package com.driff.android.module.domain.interactor

import android.util.Log
import com.driff.android.module.data.repository.ImageLoaderRepository
import com.driff.android.module.data.repository.NasaPicturesRepository
import com.driff.android.module.domain.model.asExternalModel
import com.driff.android.module.domain.model.entity.PictureOfDay
import kotlinx.coroutines.*

class GetNasaPictureOfDayUseCase(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val repository: NasaPicturesRepository,
    private val imageLoaderRepository: ImageLoaderRepository
) {
    suspend operator fun invoke(refresh: Boolean = false): Result<PictureOfDay> =
        withContext(defaultDispatcher) {
            Log.d(this::class.simpleName, "Coroutine name: ${coroutineContext.isActive}")
            repository.fetchNasaPictureOfTheDay(refresh)
                .mapCatching {
                    val image = async { imageLoaderRepository.getImage(it.url) }
                    image.await().let { byteArray ->
                        it.asExternalModel(byteArray.getOrNull())
                    }
                }
                .onFailure {
                    Log.e(this@GetNasaPictureOfDayUseCase::class.simpleName, it.cause.toString())
                    Log.e(this@GetNasaPictureOfDayUseCase::class.simpleName, "Coroutine name: ${coroutineContext[CoroutineName]}")
                    Log.e(GetNasaPictureOfDayUseCase::class.simpleName, "${it.message.orEmpty()} - ${it.cause}")
                }
        }

//    private suspend fun getImageFromUrl(url: String): ByteArray? =
//        .fold(
//            {it}, {null}
//        )
}
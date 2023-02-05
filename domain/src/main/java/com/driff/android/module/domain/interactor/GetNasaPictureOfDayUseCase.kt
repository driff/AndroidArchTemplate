package com.driff.android.module.domain.interactor

import com.driff.android.module.data.model.entity.NasaPicture
import com.driff.android.module.data.repository.NasaPicturesRepository
import com.driff.android.module.domain.model.asExternalModel
import com.driff.android.module.domain.model.entity.PictureOfDay

class GetNasaPictureOfDayUseCase(private val repository: NasaPicturesRepository) {
    suspend operator fun invoke(): Result<PictureOfDay> {
        return repository.fetchNasaPictureOfTheDay()
            .map(NasaPicture::asExternalModel)
    }
}
package com.driff.android.modulartemplate.di

import com.driff.android.module.data.repository.ImageLoaderRepository
import com.driff.android.module.data.repository.NasaPicturesRepository
import com.driff.android.module.domain.interactor.GetNasaPictureOfDayUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(ActivityRetainedComponent::class)
object DomainModule {

    @Provides
    fun providePictureOfDayUseCase(
        @IODispatcher dispatcher: CoroutineDispatcher,
        nasaRepository: NasaPicturesRepository,
        imageRepository: ImageLoaderRepository
    ) =
        GetNasaPictureOfDayUseCase(dispatcher, nasaRepository, imageRepository)

}
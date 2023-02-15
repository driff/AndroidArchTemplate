package com.driff.android.modulartemplate.di

import com.driff.android.modulartemplate.scope.RepositoryScope
import com.driff.android.module.data.api.ImageLoaderApi
import com.driff.android.module.data.api.NasaPicturesApi
import com.driff.android.module.data.datasource.remote.ImageRemoteDataSource
import com.driff.android.module.data.datasource.remote.NasaPicturesRemoteDataSource
import com.driff.android.module.data.repository.ImageLoaderRepository
import com.driff.android.module.data.repository.NasaPicturesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideNasaPicturesRepo(dataSource: NasaPicturesRemoteDataSource, repositoryScope: RepositoryScope) =
        NasaPicturesRepository(dataSource)


    @Provides
    @Singleton
    fun provideImageLoaderRepo(dataSource: ImageRemoteDataSource, repositoryScope: RepositoryScope) =
        ImageLoaderRepository(dataSource)

    @Provides
    fun provideImageDataSource(
        api: ImageLoaderApi,
        @IODispatcher dispatcher: CoroutineDispatcher
    ) = ImageRemoteDataSource(api, dispatcher)

    @Provides
    fun provideNasaRemoteDataSource(
        api: NasaPicturesApi,
    ) = NasaPicturesRemoteDataSource(api)

}
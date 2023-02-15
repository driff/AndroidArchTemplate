package com.driff.android.modulartemplate.di

import android.util.Log
import com.driff.android.modulartemplate.providers.FetchImageApi
import com.driff.android.modulartemplate.providers.NasaApi
import com.driff.android.module.data.api.ImageLoaderApi
import com.driff.android.module.data.api.NasaPicturesApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.observer.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import java.util.*
import javax.inject.Qualifier
import javax.net.ssl.*

@Module
@InstallIn(SingletonComponent::class)
object ConnectionModule {

    @Provides
    @IODispatcher
    fun provideIODispatcher() = Dispatchers.IO

    @Provides
    @MainDispatcher
    fun provideMainDispatcher() = Dispatchers.Main

    @Provides
    @DefaultDispatcher
    fun provideDefaultDispatcher() = Dispatchers.Default

}

@Module
@InstallIn(SingletonComponent::class)
interface SingletonBinding {

    @Binds
    fun nasaApiBinding(api: NasaApi): NasaPicturesApi

    @Binds
    fun imageLoaderBinding(api: FetchImageApi): ImageLoaderApi


}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IODispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultDispatcher
package com.driff.android.modulartemplate.di

import com.driff.android.modulartemplate.api.implementation.NasaApi
import com.driff.android.module.data.api.NasaPicturesApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.logging.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConnectionModule {

    @Provides
    @Singleton
    fun provideKtorClient() = HttpClient(OkHttp) {
        install(Logging)
        engine {
            config {
                followSslRedirects(true)
            }
        }
    }

}

@Module
@InstallIn(SingletonComponent::class)
interface SingletonBinding {

    @Binds
    @Singleton
    fun nasaApiBinding(api: NasaApi): NasaPicturesApi

}
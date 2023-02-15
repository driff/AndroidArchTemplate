package com.driff.android.modulartemplate.di

import com.driff.android.modulartemplate.api.implementation.FetchImageApi
import com.driff.android.modulartemplate.api.implementation.NasaApi
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
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import java.security.KeyStore
import java.util.*
import javax.inject.Qualifier
import javax.inject.Singleton
import javax.net.ssl.*

@Module
@InstallIn(SingletonComponent::class)
object ConnectionModule {

    @Provides
    fun provideKtorClient() = HttpClient(OkHttp) {
        expectSuccess = false
        install(Logging)
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
        engine {
            config {
                followSslRedirects(true)
            }
        }
    }

//    @Provides
//    fun provideTrustManagerFactory(): Array<TrustManager> = TrustManagerFactory
//        .getInstance(TrustManagerFactory.getDefaultAlgorithm())
//        .apply {
//            init(null as KeyStore?)
//        }.trustManagers
//
//    @Provides
//    fun provideSSLSocketFactory(managers: Array<TrustManager>): SSLSocketFactory {
//        if (managers.size != 1 || managers[0] !is X509TrustManager) {
//            throw IllegalStateException("Unexpected default trust managers:" + Arrays.toString(managers))
//        }
//        val trustManager = managers[0] as X509TrustManager
//        val sslContext = SSLContext.getInstance("SSL")
//        sslContext.init(null, arrayOf(trustManager), null)
//        val sslSocketFactory = sslContext.socketFactory
//    }


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
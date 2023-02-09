package com.driff.android.modulartemplate.api.implementation

import com.driff.android.modulartemplate.BuildConfig
import com.driff.android.module.data.api.NasaPicturesApi
import com.driff.android.module.data.model.exception.BadRequestException
import com.driff.android.module.data.model.remote.RemoteNasaPicture
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Inject

class NasaApi @Inject constructor(private val client: HttpClient): NasaPicturesApi {

    override suspend fun fetchPictureOfTheDay(): RemoteNasaPicture {
        client.use {
            val response = it.get("https://api.nasa.gov/planetary/apod") {
                url {
                    parameters.append(name = "api_key", value = BuildConfig.NASA_API_KEY)
                }
            }
            if(response.status.value in 200 .. 299)
                return response.body()
            else
                throw BadRequestException(response.status.description)
        }
    }

    override suspend fun fetchPicturesFromRange(from: String, to: String): List<RemoteNasaPicture> {
        TODO("Not yet implemented")
    }


}
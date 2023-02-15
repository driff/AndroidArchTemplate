package com.driff.android.modulartemplate.api.implementation

import android.util.Log
import com.driff.android.modulartemplate.BuildConfig
import com.driff.android.module.data.api.NasaPicturesApi
import com.driff.android.module.data.model.exception.BadRequestException
import com.driff.android.module.data.model.remote.RemoteNasaPicture
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import javax.inject.Inject

class NasaApi @Inject constructor(
    private val client: HttpClient,
): NasaPicturesApi {

    override suspend fun fetchPictureOfTheDay(): RemoteNasaPicture {
        val response = client.use {
            it.get(("https://api.nasa.gov/planetary/apod")) {
                Log.d(this@NasaApi::class.simpleName, "Coroutine name: ${this.executionContext.isActive}")
                url {
                    parameters.append(name = "api_key", value = BuildConfig.NASA_API_KEY)
                }
            }

        }
        if(response.status.value in 200 .. 299) {
            return response.body()
        }
        else {
            throw BadRequestException(response.status.description)
        }
    }

    override suspend fun fetchPicturesFromRange(from: String, to: String): List<RemoteNasaPicture> {
        TODO("Not yet implemented")
    }


}
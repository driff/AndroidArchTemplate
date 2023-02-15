package com.driff.android.modulartemplate.providers

import com.driff.android.module.data.api.ImageLoaderApi
import io.ktor.client.call.*
import io.ktor.client.request.*
import javax.inject.Inject
import kotlin.io.use

class FetchImageApi @Inject constructor(private val networkClient: NetworkClient): ImageLoaderApi {
    override suspend fun fetchImage(url: String): ByteArray = networkClient().use { client ->
        val response = client.get(url)
        return response.body()
    }
}
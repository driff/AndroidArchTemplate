package com.driff.android.modulartemplate.api.implementation

import com.driff.android.module.data.api.ImageLoaderApi
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import javax.inject.Inject

class FetchImageApi @Inject constructor(private val client: HttpClient): ImageLoaderApi {
    override suspend fun fetchImage(url: String): ByteArray {
        client.use {ktor ->
            val response = ktor.get(url)
            val byteArrayBody: ByteArray = response.body()
            return byteArrayBody
        }
    }
}
package com.driff.android.module.data.api

interface ImageLoaderApi {

    suspend fun fetchImage(url: String): ByteArray

}
package com.driff.android.module.data.api

import com.driff.android.module.data.remote.model.RemotePictureOfDay

interface NasaPicturesApi {

    suspend fun fetchPictureOfTheDay(): RemotePictureOfDay

    suspend fun fetchPicturesFromRange(from: String, to: String): List<RemotePictureOfDay>

}
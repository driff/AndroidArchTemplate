package com.driff.android.module.data.api

import com.driff.android.module.data.model.remote.RemoteNasaPicture

interface NasaPicturesApi {

    suspend fun fetchPictureOfTheDay(): RemoteNasaPicture

    suspend fun fetchPicturesFromRange(from: String, to: String): List<RemoteNasaPicture>

}
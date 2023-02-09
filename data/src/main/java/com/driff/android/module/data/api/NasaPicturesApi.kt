package com.driff.android.module.data.api

import com.driff.android.module.data.datasource.remote.NasaPictureRemoteModel


interface NasaPicturesApi {

    suspend fun fetchPictureOfTheDay(): NasaPictureRemoteModel

    suspend fun fetchPicturesFromRange(from: String, to: String): List<NasaPictureRemoteModel>

}
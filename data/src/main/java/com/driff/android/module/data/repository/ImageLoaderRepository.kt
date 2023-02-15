package com.driff.android.module.data.repository

import com.driff.android.module.data.datasource.remote.ImageRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import java.net.URL

class ImageLoaderRepository constructor(
    private val dataSource: ImageRemoteDataSource
) {

    suspend fun getImage(url: String): Result<ByteArray> =
            dataSource.fetchImage(url)

}
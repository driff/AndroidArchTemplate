package com.driff.android.module.data.datasource.remote

import com.driff.android.module.MainDispatcherRule
import com.driff.android.module.data.api.ImageLoaderApi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NasaRetrieveImageDataSourceTest {

    @get:Rule
    val mockkRule = MockKRule(this)
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @RelaxedMockK
    private lateinit var imageApi: ImageLoaderApi

    lateinit var dataSource: ImageRemoteDataSource

    @Before
    fun setup() {
        coEvery { imageApi.fetchImage(any()) } coAnswers { byteArrayOf(1, 2, 3, 4) }
        dataSource = ImageRemoteDataSource(imageApi, mainDispatcherRule.testDispatcher)
    }

    @Test
    fun shouldCallAPI() = runTest {
        // GIVEN remoteDataSource instance
        // WHEN fetch image is invoked
        dataSource.fetchImage("an url goes here")
        // THEN it should call NasaPicturesApi
        coVerify(exactly = 1) { imageApi.fetchImage(any()) }
    }

    @Test
    fun shouldReturnSuccessByteArray() = runTest {
        // GIVEN remoteDataSource instance
        // WHEN fetch image is invoked
        val result = dataSource.fetchImage("an url goes here")
        // THEN it should call NasaPicturesApi
        assertTrue(result.isSuccess)
        assertEquals(1.toByte(), result.getOrNull()?.get(0))
    }

}
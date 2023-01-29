package com.driff.android.module.data.datasource

import com.driff.android.module.data.api.NasaPicturesApi
import com.driff.android.module.data.datasource.RemoteDataDummies.successRemotePictureOfDay
import com.driff.android.module.data.remote.model.RemotePictureOfDay
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import kotlinx.coroutines.*
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
class NasaPicturesRemoteDataSourceTest {
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private lateinit var dispatcher: CoroutineDispatcher

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var nasaApi: NasaPicturesApi

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun shouldCallAPI() = runTest {
        // GIVEN remoteDataSource instance
        val dispatcher = StandardTestDispatcher(testScheduler)
        val dataSource = NasaPicturesRemoteDataSource(nasaApi, dispatcher)
        coEvery { nasaApi.fetchPictureOfTheDay() } coAnswers { successRemotePictureOfDay }
        // WHEN fetchPictureOfTheDay is invoked
        dataSource.fetchPictureOfTheDay()
        // THEN it should call NasaPicturesApi
        coVerify(exactly = 1) { nasaApi.fetchPictureOfTheDay() }
    }

}
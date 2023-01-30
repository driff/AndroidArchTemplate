package com.driff.android.module.data.datasource

import com.driff.android.module.data.api.NasaPicturesApi
import com.driff.android.module.data.RemoteDataDummies.successRemoteNasaPicture
import com.driff.android.module.data.exception.BadRequestException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.*
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
class NasaPicturesRemoteDataSourceTest {
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var nasaApi: NasaPicturesApi

    lateinit var dispatcher: CoroutineDispatcher
    lateinit var dataSource: NasaPicturesRemoteDataSource

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
        dispatcher = StandardTestDispatcher(testScheduler)
        dataSource = NasaPicturesRemoteDataSource(nasaApi, dispatcher)
        // WHEN fetchPictureOfTheDay is invoked
        dataSource.fetchPictureOfTheDay()
        // THEN it should call NasaPicturesApi
        coVerify(exactly = 1) { nasaApi.fetchPictureOfTheDay() }
    }

    @Test
    fun shouldReturnSuccess() = runTest {
        // GIVEN remoteDataSource instance
        val dispatcher = StandardTestDispatcher(testScheduler)
        val dataSource = NasaPicturesRemoteDataSource(nasaApi, dispatcher)
        // WHEN fetchPictureOfTheDay is invoked
        // AND Api Answers with success
        coEvery { nasaApi.fetchPictureOfTheDay() } coAnswers { successRemoteNasaPicture }
        val response = dataSource.fetchPictureOfTheDay()
        // THEN it should return a Success Result
        assertTrue(response.isSuccess)
    }

    @Test
    fun successShouldReturnData() = runTest {
        // GIVEN remoteDataSource instance
        val dispatcher = StandardTestDispatcher(testScheduler)
        val dataSource = NasaPicturesRemoteDataSource(nasaApi, dispatcher)
        // WHEN fetchPictureOfTheDay is invoked
        // AND Api Answers with success
        coEvery { nasaApi.fetchPictureOfTheDay() } coAnswers { successRemoteNasaPicture }
        val response = dataSource.fetchPictureOfTheDay()
        // THEN it should return a Success Result
        assertNotNull(response.getOrNull())
    }

    @Test
    fun shouldReturnFailure() = runTest {
        // GIVEN remoteDataSource instance
        val dispatcher = StandardTestDispatcher(testScheduler)
        val dataSource = NasaPicturesRemoteDataSource(nasaApi, dispatcher)
        // WHEN fetchPictureOfTheDay is invoked
        // AND Api Answers with exception
        val exceptionMessage = "Date must be between Jun 16, 1995 and Jan 29, 2023"
        coEvery { nasaApi.fetchPictureOfTheDay() } throws BadRequestException(exceptionMessage)
        val response = dataSource.fetchPictureOfTheDay()
        // THEN it should return a Failure Result
        assertTrue(response.isFailure)
        assertEquals(exceptionMessage, response.exceptionOrNull()?.message)
    }

    @Test
    fun shouldReturnThrownException() = runTest {
        // GIVEN remoteDataSource instance
        val dispatcher = StandardTestDispatcher(testScheduler)
        val dataSource = NasaPicturesRemoteDataSource(nasaApi, dispatcher)
        // WHEN fetchPictureOfTheDay is invoked
        // AND Api Answers with exception
        val exceptionMessage = "Date must be between Jun 16, 1995 and Jan 29, 2023"
        val exception = BadRequestException(exceptionMessage)
        coEvery { nasaApi.fetchPictureOfTheDay() } throws exception
        val response = dataSource.fetchPictureOfTheDay()
        // THEN it should return the thrown exception
        assertTrue(response.exceptionOrNull() is BadRequestException)
        assertEquals(exception, response.exceptionOrNull())
    }

}
package com.driff.android.module.data.repository

import com.driff.android.module.MainDispatcherRule
import com.driff.android.module.data.RemoteDataDummies.successRemoteNasaPicture
import com.driff.android.module.data.remote.datasource.NasaPicturesRemoteDataSource
import com.driff.android.module.data.remote.model.RemoteNasaPicture
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RemoteNasaPicturesRepositoryTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    lateinit var testScope: TestScope

    @RelaxedMockK
    lateinit var dataSource: NasaPicturesRemoteDataSource

    private lateinit var repository: NasaPicturesRepository

    @Before
    fun setup() {
        testScope = TestScope(mainDispatcherRule.testDispatcher)
        repository = NasaPicturesRepository(dataSource, testScope)
    }

    @After
    fun runAfter() {

    }

    @Test
    fun shouldCallRemoteDataSource() = runTest {
        backgroundScope
        // GIVEN repository instance
        // WHEN fetchPictureOfDay is invoked
        // AND cache is empty
        // AND datasource responds with valid data
        coEvery { dataSource.fetchPictureOfTheDay() } coAnswers {
            Result.success(successRemoteNasaPicture)
        }
        repository.fetchNasaPictureOfTheDay()
        // THEN repository should call datasource
        coVerify { dataSource.fetchPictureOfTheDay() }
    }

    @Test
    fun shouldFetchDataOnce() = runTest {
        // GIVEN repository instance
        val modifiedDataSourceResponse = Result.failure<RemoteNasaPicture>(NullPointerException())
        // WHEN fetchPictureOfDay is invoked
        // AND datasource responds with valid data
        coEvery { dataSource.fetchPictureOfTheDay() } coAnswers {
            Result.success(successRemoteNasaPicture)
        }
        // AND cache is not empty
        repository.fetchNasaPictureOfTheDay()
        // AND refresh is false
        // AND fetchPictureOfDay is invoked a second time
        coEvery { dataSource.fetchPictureOfTheDay() } coAnswers { modifiedDataSourceResponse }
        repository.fetchNasaPictureOfTheDay()
        // THEN repository should call fetchPictureOfTheDay only once
        coVerify(exactly = 1) { dataSource.fetchPictureOfTheDay() }
    }

    @Test
    fun shouldReturnCachedData() = runTest {
        // GIVEN repository instance
        // WHEN fetchPictureOfDay is invoked
        // AND datasource responds with valid data
        val modifiedDataSourceResponse = Result.success(
            successRemoteNasaPicture.copy("2023-01-30")
        )
        coEvery { dataSource.fetchPictureOfTheDay() } coAnswers {
            Result.success(successRemoteNasaPicture)
        }
        // AND cache is not empty
        repository.fetchNasaPictureOfTheDay()
        // AND refresh is false
        // AND fetchPictureOfDay is invoked a second time
        coEvery { dataSource.fetchPictureOfTheDay() } coAnswers { modifiedDataSourceResponse }
        val response = repository.fetchNasaPictureOfTheDay()
        // THEN repository should return cached response
        assertEquals("2023-01-28", response.getOrNull()?.date)
    }

    @Test
    fun shouldFetchDataAgain() = runTest {
        // GIVEN repository instance
        val modifiedDataSourceResponse = Result.success(
            successRemoteNasaPicture.copy("2023-01-30")
        )
        // WHEN fetchPictureOfDay is invoked
        // AND datasource responds with valid data
        coEvery { dataSource.fetchPictureOfTheDay() } coAnswers {
            Result.success(successRemoteNasaPicture)
        }
        // AND cache is not empty
        repository.fetchNasaPictureOfTheDay()
        // AND fetchPictureOfDay is invoked a second time
        coEvery { dataSource.fetchPictureOfTheDay() } coAnswers { modifiedDataSourceResponse }
        // AND refresh is true
        repository.fetchNasaPictureOfTheDay(true)
        // THEN repository should call fetchPictureOfTheDay only once
        coVerify(exactly = 2) { dataSource.fetchPictureOfTheDay() }
    }

    @Test
    fun shouldReturnFailure() = runTest {
        // GIVEN repository instance
        // WHEN fetchPictureOfDay is invoked
        // AND datasource responds with exception
        coEvery { dataSource.fetchPictureOfTheDay() } throws NullPointerException("Something not null was null")
        // AND cache is empty
        val result = repository.fetchNasaPictureOfTheDay()
        // THEN result should be a failure
        assertTrue(result.isFailure)
    }

    @Test
    fun jobShouldCancel() = runTest {
        // GIVEN repository instance
        // WHEN fetchPictureOfDay is invoked
        // AND datasource responds with CancellationException
        coEvery { dataSource.fetchPictureOfTheDay() } throws CancellationException("Cancelling Job")
        // AND cache is empty
        // THEN coroutine job should be cancelled
        val job = launch {
            repository.fetchNasaPictureOfTheDay()
        }
        advanceUntilIdle()
        assertTrue(job.isCancelled)
    }

    @Test
    fun shouldUpdateCacheWhenCancelled() = runTest(UnconfinedTestDispatcher()) {
        val modifiedDataSourceResponse = Result.success(
            successRemoteNasaPicture.copy("2023-01-30")
        )
        // GIVEN repository instance
        // WHEN response is success
        coEvery { dataSource.fetchPictureOfTheDay() } coAnswers {
            Result.success(successRemoteNasaPicture)
        }
        // AND fetchPictureOfDay is invoked
        val job = launch { repository.fetchNasaPictureOfTheDay() }
        // AND request is cancelled
        job.cancel("User moved to another screen")
        // AND  after some time fetchPictureOfDay is invoked a second time with reset false
        advanceUntilIdle()
        coEvery { dataSource.fetchPictureOfTheDay() } coAnswers { modifiedDataSourceResponse }
        val response = repository.fetchNasaPictureOfTheDay(false)
        // THEN response should return cached data
        assertEquals("2023-01-28", response.getOrNull()?.date)
    }

}
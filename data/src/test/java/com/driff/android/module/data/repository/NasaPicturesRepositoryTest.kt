package com.driff.android.module.data.repository

import com.driff.android.module.MainDispatcherRule
import com.driff.android.module.data.RemoteDataDummies.successRemoteNasaPicture
import com.driff.android.module.data.datasource.remote.NasaPicturesRemoteDataSource
import com.driff.android.module.data.model.remote.RemoteNasaPicture
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RemoteModelNasaPicturesRepositoryTest {

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
        repository =
            com.driff.android.module.data.repository.NasaPicturesRepository(dataSource, testScope)
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
    fun jobShouldCancelImmediately() = runTest(StandardTestDispatcher()) {
        // GIVEN repository instance
        // WHEN fetchPictureOfDay is invoked
        // AND datasource responds with success
        coEvery { dataSource.fetchPictureOfTheDay() } coAnswers {
            delay(1000)
            Result.success(successRemoteNasaPicture)
        }
        // AND cache is empty
        // AND job is cancelled
        val job = launch {
            repository.fetchNasaPictureOfTheDay()
        }
        job.cancel("Cancelled parent thread")
        // THEN is cancelled should be true
        assertTrue(job.isCancelled)
        job.children.toList().forEach {
            assertTrue(it.isCancelled)
        }
    }

    @Test
    fun jobShouldCancel() = runTest {
        // GIVEN repository instance
        // WHEN fetchPictureOfDay is invoked
        // AND datasource responds with CancellationException
        coEvery { dataSource.fetchPictureOfTheDay() } coAnswers {
            Result.success(successRemoteNasaPicture)
        }
        // AND cache is empty
        // THEN coroutine job should be cancelled
        val job = launch {
            repository.fetchNasaPictureOfTheDay()
        }
        job.cancel(CancellationException("Cancelling Job"))
        assertTrue(job.isCancelled)
        job.children.forEach {
            assertTrue(it.isCancelled)
        }
    }

    @Test
    fun jobShouldCallDatasourceWhenCancelled() = runTest(StandardTestDispatcher()) {
        // GIVEN repository instance
        // WHEN fetchPictureOfDay is invoked
        // AND datasource responds with CancellationException
        coEvery { dataSource.fetchPictureOfTheDay() } coAnswers {
            delay(1000)
            Result.success(successRemoteNasaPicture)
        }
        // AND cache is empty
        // THEN coroutine job should be cancelled
        launch {
            repository.fetchNasaPictureOfTheDay()
        }
        yield()
        testScheduler.cancel()
        advanceUntilIdle()
        coVerify(exactly = 1) { dataSource.fetchPictureOfTheDay() }
    }

    @Test
    fun shouldUpdateCacheWhenCancelled() = runTest(StandardTestDispatcher()) {
        val modifiedDataSourceResponse = Result.success(
            successRemoteNasaPicture.copy("2023-01-30")
        )
        // GIVEN repository instance
        // WHEN response is success
        coEvery { dataSource.fetchPictureOfTheDay() } coAnswers {
            delay(1000)
            Result.success(successRemoteNasaPicture)
        }
        // AND fetchPictureOfDay is invoked
        val job = launch { repository.fetchNasaPictureOfTheDay() }
        yield()
        // AND request is cancelled
        job.cancel("User moved to another screen")
        // AND  after some time fetchPictureOfDay is invoked a second time with reset false
        advanceUntilIdle()
        coEvery { dataSource.fetchPictureOfTheDay() } coAnswers { modifiedDataSourceResponse }
        val response = repository.fetchNasaPictureOfTheDay(false)
        // THEN response should return cached data
        coVerify(exactly = 1) { dataSource.fetchPictureOfTheDay() }
        assertEquals("2023-01-28", response.getOrNull()?.date)
    }

    @Test
    fun shouldInvokeDataSourceOnce() = runTest(StandardTestDispatcher()) {
        // GIVEN repository instance
        // WHEN response is success
        coEvery { dataSource.fetchPictureOfTheDay() } coAnswers {
            delay(10_000)
            Result.success(successRemoteNasaPicture)
        }
        val scheduler = testScheduler // the scheduler used for this test
        val dispatcher1 = StandardTestDispatcher(scheduler, name = "IO dispatcher")
        val dispatcher2 = StandardTestDispatcher(scheduler, name = "Background dispatcher")
        // AND fetchPictureOfDay is invoked
        launch { repository.fetchNasaPictureOfTheDay() }
        // AND is invoked multiple times
        launch(dispatcher1) { repository.fetchNasaPictureOfTheDay() }
        launch(dispatcher2) { repository.fetchNasaPictureOfTheDay() }
        launch(mainDispatcherRule.testDispatcher) { repository.fetchNasaPictureOfTheDay() }
        advanceUntilIdle()
        // THEN datasource shoudl've been invoked once
        coVerify(exactly = 1) { dataSource.fetchPictureOfTheDay() }

    }

}
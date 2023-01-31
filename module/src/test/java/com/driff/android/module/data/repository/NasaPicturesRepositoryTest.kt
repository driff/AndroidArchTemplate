package com.driff.android.module.data.repository

import com.driff.android.module.BaseTestSetup
import com.driff.android.module.data.RemoteDataDummies
import com.driff.android.module.data.RemoteDataDummies.successRemoteNasaPicture
import com.driff.android.module.data.api.NasaPicturesApi
import com.driff.android.module.data.remote.datasource.NasaPicturesRemoteDataSource
import com.driff.android.module.data.remote.model.RemoteNasaPicture
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
class RemoteNasaPicturesRepositoryTest: BaseTestSetup() {

    @get:Rule
    val mockkRule = MockKRule(this)

    lateinit var dispatcher: CoroutineDispatcher
    @RelaxedMockK
    lateinit var dataSource: NasaPicturesRemoteDataSource

    @Test
    fun shouldCallRemoteDataSource() = runTest {
        // GIVEN repository instance
        val repository = NasaPicturesRepository(dataSource)
        // WHEN fetchPictureOfDay is invoked
        // AND cache is empty
        // AND datasource responds with valid data
        coEvery { dataSource.fetchPictureOfTheDay() } coAnswers {
            Result.success(RemoteDataDummies.successRemoteNasaPicture)
        }
        repository.fetchNasaPictureOfTheDay()
        // THEN repository should call datasource
        coVerify { dataSource.fetchPictureOfTheDay() }
    }

    @Test
    fun shouldFetchDataOnce() = runTest {
        // GIVEN repository instance
        val modifiedDataSourceResponse = Result.failure<RemoteNasaPicture>(NullPointerException())
        val repository = NasaPicturesRepository(dataSource)
        // WHEN fetchPictureOfDay is invoked
        // AND datasource responds with valid data
        coEvery { dataSource.fetchPictureOfTheDay() } coAnswers {
            Result.success(RemoteDataDummies.successRemoteNasaPicture)
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
        val modifiedDataSourceResponse = Result.success(
            successRemoteNasaPicture.copy("2023-01-30")
        )
        val repository = NasaPicturesRepository(dataSource)
        // WHEN fetchPictureOfDay is invoked
        // AND datasource responds with valid data
        coEvery { dataSource.fetchPictureOfTheDay() } coAnswers {
            Result.success(RemoteDataDummies.successRemoteNasaPicture)
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
        val repository = NasaPicturesRepository(dataSource)
        // WHEN fetchPictureOfDay is invoked
        // AND datasource responds with valid data
        coEvery { dataSource.fetchPictureOfTheDay() } coAnswers {
            Result.success(RemoteDataDummies.successRemoteNasaPicture)
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

}
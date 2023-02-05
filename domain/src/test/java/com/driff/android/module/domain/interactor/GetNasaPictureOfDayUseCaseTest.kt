package com.driff.android.module.domain.interactor

import com.driff.android.module.data.model.entity.NasaPicture
import com.driff.android.module.data.repository.NasaPicturesRepository
import com.driff.android.module.domain.Dummies.SuccessNasaPicture
import com.driff.android.module.domain.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetNasaPictureOfDayUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    private lateinit var testScope: TestScope

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @RelaxedMockK
    lateinit var repository: NasaPicturesRepository

    lateinit var useCase: GetNasaPictureOfDayUseCase

    @Before
    fun setup() {
        testScope = TestScope(mainDispatcherRule.testDispatcher)
        coEvery { repository.fetchNasaPictureOfTheDay() }coAnswers {
            Result.success(SuccessNasaPicture)
        }
        useCase = GetNasaPictureOfDayUseCase(repository)
    }

    @Test
    fun shouldCallRepository() = runTest {
        // GIVEN use case instance
        // WHEN it is invoked
        useCase()
        // THEN it should call NasaPicturesRepository
        coVerify { repository.fetchNasaPictureOfTheDay() }
    }

    @Test
    fun shouldReturnNasaPicture() = runTest {
        // GIVEN use case instance
        // WHEN it is invoked AND response is success
        val result = useCase()
        // THEN it should return Success Result with a picture of day object
        assertTrue(result.isSuccess)
        assertEquals(SuccessNasaPicture.url, result.getOrNull()?.url)
    }

    @Test
    fun shouldRefreshWhenDateChanges() = runTest {
        // GIVEN use case instance
        // WHEN it is invoked AND response is success
        val result = useCase()
        // AND date is current day
        // THEN it should return request with reset false
        assertTrue(result.isSuccess)
        assertEquals(SuccessNasaPicture.url, result.getOrNull()?.url)
    }

}
package com.driff.android.module.data.repository

import com.driff.android.module.MainDispatcherRule
import com.driff.android.module.data.datasource.remote.ImageRemoteDataSource
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
class ImageLoaderRepositoryTest {
    @get:Rule
    val mockkRule = MockKRule(this)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var testScope: TestScope

    @RelaxedMockK
    lateinit var dataSource: ImageRemoteDataSource

    private lateinit var repository: ImageLoaderRepository

    @Before
    fun setup() {
        testScope = TestScope(mainDispatcherRule.testDispatcher)
        repository = ImageLoaderRepository(testScope, dataSource)
    }

    @Test
    fun shouldCallDataSource() = runTest {
        // GIVEN repository instance
        // WHEN getImage is invoked
        repository.getImage("this/is/a/url")
        // THEN it should call data source
        coVerify(exactly = 1) { repository.getImage(any()) }
    }

}
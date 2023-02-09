package com.driff.android.module.domain.interactor

import com.driff.android.module.domain.usecase.GetNasaDateFormatterUseCase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

class GetNasaFormattedDateTest {

    @Test
    fun shouldReturnNonEmptyString() {
        // GIVEN use case Instance
        val useCase = GetNasaDateFormatterUseCase()
        // WHEN invoked
        // with current day date
        val date = Calendar.getInstance().time
        val result = useCase(date)
        // THEN result should be a non empty string
        assertTrue(result.isNotEmpty())
    }

    @Test
    fun shouldReturnValidFormattedDate() {
        // GIVEN use case Instance
        val useCase = GetNasaDateFormatterUseCase()
        // WHEN invoked
        // with current day date
        val date = Calendar.getInstance().apply {
            set(2023, 1, 10)
        }.time
        val result = useCase(date)
        // THEN result should be a non empty string
        assertEquals("2023-02-10", result)
    }

}
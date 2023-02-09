package com.driff.android.module.domain.interactor

import java.text.SimpleDateFormat
import java.util.*

class GetNasaDateFormatterUseCase {

    private val nasaDateFormat =  SimpleDateFormat("yyyy-MM-dd", Locale.US)

    operator fun invoke(date: Date): String {
        return nasaDateFormat.format(date)
    }

}
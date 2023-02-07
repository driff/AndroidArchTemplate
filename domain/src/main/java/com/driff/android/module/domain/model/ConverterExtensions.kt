package com.driff.android.module.domain.model

import com.driff.android.module.data.model.entity.NasaPicture
import com.driff.android.module.domain.model.entity.PictureOfDay


fun NasaPicture.asExternalModel(byteArray: ByteArray?): PictureOfDay = PictureOfDay(
    date = date,
    mediaType = mediaType,
    title = title,
    description = explanation
)


package com.driff.android.module.data.utils

import com.driff.android.module.data.remote.model.RemoteNasaPicture
import com.driff.android.module.data.repository.entities.NasaPicture

fun RemoteNasaPicture.asExternalModel() = NasaPicture(
    date = date,
    mediaType = mediaType,
    hdurl = hdurl,
    explanation = explanation,
    title = title,
    url = url,
)


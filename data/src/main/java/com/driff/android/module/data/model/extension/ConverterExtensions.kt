package com.driff.android.module.data.utils

import com.driff.android.module.data.model.remote.RemoteNasaPicture
import com.driff.android.module.data.model.entity.NasaPicture

fun RemoteNasaPicture.asExternalModel() = NasaPicture(
    date = date,
    mediaType = mediaType,
    hdurl = hdurl,
    explanation = explanation,
    title = title,
    url = url,
)


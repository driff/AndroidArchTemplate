package com.driff.android.module.data.datasource.local

import com.driff.android.module.data.model.NasaPictureModel
import kotlinx.serialization.SerialName

data class NasaPictureLocalModel(
	val date: String,
	val copyright: String,
	@SerialName("media_type")
	val mediaType: String,
	val hdurl: String,
	@SerialName("service_version")
	val serviceVersion: String,
	val explanation: String,
	val title: String,
	val url: String
)

fun NasaPictureLocalModel.toModel() = NasaPictureModel(
	date = date,
	mediaType = mediaType,
	hdurl = hdurl,
	explanation = explanation,
	title = title,
	url = url,
)

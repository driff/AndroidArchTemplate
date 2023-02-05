package com.driff.android.module.data.model.remote

import kotlinx.serialization.SerialName

data class RemoteNasaPicture(
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


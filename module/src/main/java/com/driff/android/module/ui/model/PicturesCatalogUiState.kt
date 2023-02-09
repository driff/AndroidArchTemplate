package com.driff.android.module.ui.model

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PicturesCatalogUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val imageItem: ImageItemUiState = ImageItemUiState(),
): Parcelable

@Parcelize
data class ImageItemUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val image: Bitmap? = null,
    val description: String = "",
    val author: String = "",
    val title: String = ""
): Parcelable
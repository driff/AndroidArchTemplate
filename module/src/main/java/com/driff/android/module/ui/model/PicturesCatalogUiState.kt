package com.driff.android.module.ui.model

data class PicturesCatalogUiState(
    val errorMessage: String? = null,
    val imageItem: ImageItemUiState? = null,
    val title: String
)

data class ImageItemUiState(
    val isLoading: Boolean,
    val errorMessage: String? = null,
    val url: String,
    val hdUrl: String,
    val description: String,
    val author: String,
    val title: String
)
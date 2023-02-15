package com.driff.android.module.ui.picture

import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.driff.android.module.domain.interactor.GetNasaPictureOfDayUseCase
import com.driff.android.module.ui.model.ImageItemUiState
import com.driff.android.module.ui.model.PicturesCatalogUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

//https://developer.android.com/topic/libraries/architecture/viewmodel#best-practices

@HiltViewModel
class PicturesViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val pictureOfDayUseCase: GetNasaPictureOfDayUseCase,
): ViewModel() {

    private val _picturesCatalogUiStateFlow = MutableStateFlow(PicturesCatalogUiState())
    val picturesCatalogUiStateFlow: StateFlow<PicturesCatalogUiState> = _picturesCatalogUiStateFlow

    fun getPictures(refresh: Boolean) {
        viewModelScope.launch {
            Log.d(this::class.simpleName, "Coroutine name: ${coroutineContext.isActive}")
            pictureOfDayUseCase(refresh).mapCatching {
                val bitmap = it.imageByteArray?.let { img ->
                    async {
                        BitmapFactory.decodeByteArray(img, 0, img.size)
                    }
                }
                _picturesCatalogUiStateFlow.getAndUpdate { current ->
                    current.copy(
                        isLoading = false,
                        imageItem = ImageItemUiState(
                            isLoading = false,
                            image = bitmap?.await(),
                            title = it.title,
                            description = it.description
                        )
                    )
                }
            }
        }
    }

}
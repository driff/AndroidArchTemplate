package com.driff.android.module.ui.picture

import androidx.lifecycle.SavedStateHandle
import com.driff.android.module.domain.interactor.GetNasaPictureOfDayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PicturesViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val pictureOfDayUseCase: GetNasaPictureOfDayUseCase,
) {



}
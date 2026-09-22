package com.jcjiron.qrapp.presentation.main

import com.jcjiron.qrapp.domain.model.QrImage

sealed interface MainUiState {
    /** Checking storage for a saved image. */
    data object Loading : MainUiState

    /** No image yet: show the "load photo" button. */
    data class Empty(val saveFailed: Boolean = false) : MainUiState

    /** Show the saved image with the screen at full brightness. */
    data class ShowingImage(val image: QrImage) : MainUiState
}

package com.neomfi.microlend.presentation.util

sealed interface UiEvent {
    data object NavigateUp: UiEvent
    data class Navigate(val route: String): UiEvent
    data class ShowSnackBar(val message: String): UiEvent
    data class ShowToast(val message: String): UiEvent
}
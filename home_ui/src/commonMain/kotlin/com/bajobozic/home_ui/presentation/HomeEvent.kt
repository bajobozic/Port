package com.bajobozic.home_ui.presentation

import com.bajobozic.storage.domain.model.Movie


internal sealed interface HomeEvent {
    data class Init(val language: String, val page: Int) : HomeEvent
    data class Toggle(val toggle: Boolean) : HomeEvent
    data object OnBackPressed : HomeEvent
    data object PullToRefresh : HomeEvent
    data class NavigateToDetailsScreen(val movieId: Int) : HomeEvent
    data class ShowSnackbar(val message: String? = null, val action: (() -> Unit)? = null) :
        HomeEvent

    data class DeleteMove(val movie: Movie) : HomeEvent
    data object NavigateToSignInScreen : HomeEvent
}
package com.bajobozic.movies_ui.presentation

sealed interface MoviesEvent {
    data class Init(val data: String) : MoviesEvent
    data object OnBackPressed : MoviesEvent
    data object PullToRefresh : MoviesEvent
    data class Toggle(val id: Int) : MoviesEvent
    data class ShowSnackbar(val message: String? = null, val action: (() -> Unit)? = null) :
        MoviesEvent

    data class DeleteMovie(val movieId: Int) : MoviesEvent
    data class NavigateToDetailsScreen(val movieId: Int) : MoviesEvent
    data object NavigateToSignInScreen : MoviesEvent
}

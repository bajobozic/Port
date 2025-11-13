package com.bajobozic.port.home.presentation.component

import com.bajobozic.port.home.domain.model.Movie


sealed interface HomeAction {
    data class Init(val language: String, val page: Int) : HomeAction
    data class Toggle(val toggle: Boolean) : HomeAction
    data object OnBackPressed : HomeAction
    data object PullToRefresh : HomeAction
    data class LoadDetails(val movieId: Int) : HomeAction
    data class NavigateToDetailsScreen(val movieId: Int) : HomeAction

    data class ShowSnackbar(val message: String? = null, val action: (() -> Unit)? = null) : HomeAction
    data class DeleteMove(val movie: Movie) : HomeAction
}
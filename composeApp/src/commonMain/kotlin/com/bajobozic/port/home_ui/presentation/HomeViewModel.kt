package com.bajobozic.port.home_ui.presentation

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bajobozic.home_component.domain.usecase.GetPagingDataUseCase
import com.bajobozic.storage.domain.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

internal class HomeViewModel(
    getPagingDataUseCase: GetPagingDataUseCase,
) : ViewModel() {
    val snackbarHostState = SnackbarHostState()
    val homePagingData: Flow<PagingData<Movie>> = getPagingDataUseCase("en-US")
        .cachedIn(viewModelScope)//extremely important to cache in to viewModelScope also despite being also cached in db, this way we avoid losing scroll position when returning to the screen

    fun actionHandler(action: HomeEvent) {
        when (action) {
            is HomeEvent.Init -> {}
            HomeEvent.OnBackPressed -> {
                TODO()
            }

            HomeEvent.PullToRefresh -> {
                TODO()
            }

            is HomeEvent.Toggle -> {
                TODO()
            }

            is HomeEvent.ShowSnackbar -> {
                viewModelScope.launch {
                    snackbarHostState.showSnackbar(
                        message = action.message ?: "Snackbar is here",
                        actionLabel = "Undo",
                        duration = SnackbarDuration.Short
                    ).apply {
                        when (this) {
                            SnackbarResult.ActionPerformed -> {
                                println("Snackbar, Action Performed")

                            }

                            else -> {
                                println("Snackbar, Snackbar dismissed")
                            }
                        }
                    }
                }
            }

            is HomeEvent.DeleteMove -> {
                viewModelScope.launch {
                }
            }

            is HomeEvent.NavigateToDetailsScreen -> {
                //no op, handled in navController
            }

            HomeEvent.NavigateToSignInScreen -> {
                //no op, handled in navController
            }
        }
    }

}
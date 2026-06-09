package com.bajobozic.movies_ui.presentation

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bajobozic.movies_component.domain.usecase.GetMoviesPagingDataUseCase
import com.bajobozic.storage.domain.model.Movie
import com.bajobozic.storage.domain.usecase.DeleteMovieUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

internal class MoviesViewModel(
    getMoviesPagingDataUseCase: GetMoviesPagingDataUseCase,
    private val deleteMovieUseCase: DeleteMovieUseCase
) : ViewModel() {
    val snackbarHostState = SnackbarHostState()
    val moviesPagingData: Flow<PagingData<Movie>> = getMoviesPagingDataUseCase("en-US")
        .cachedIn(viewModelScope)

    fun actionHandler(action: MoviesEvent) {
        when (action) {
            is MoviesEvent.Init -> {}
            MoviesEvent.OnBackPressed -> {
                // Handle back press
            }

            MoviesEvent.PullToRefresh -> {
                // Handle pull to refresh
            }

            is MoviesEvent.Toggle -> {
                // Handle toggle
            }

            is MoviesEvent.ShowSnackbar -> {
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

            is MoviesEvent.DeleteMovie -> {
                viewModelScope.launch {
                    deleteMovieUseCase(action.movieId)
                }
            }

            is MoviesEvent.NavigateToDetailsScreen -> {
                //no op, handled in navController
            }

            MoviesEvent.NavigateToSignInScreen -> {
                //no op, handled in navController
            }
        }
    }
}

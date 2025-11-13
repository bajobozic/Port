package com.bajobozic.port.home.presentation

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bajobozic.port.home.domain.model.Movie
import com.bajobozic.port.home.domain.repository.HomeRepository
import com.bajobozic.port.home.presentation.component.DetailUiState
import com.bajobozic.port.home.presentation.component.HomeAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val homeRepository: HomeRepository,
) : ViewModel() {
    val snackbarHostState = SnackbarHostState()

    //we should probably do something like this if we want to change param
    /*
    var _language = MutableStateFlow("en-US")
    val language: StateFlow<String> = _language
    */
    val homePagingData: Flow<PagingData<Movie>> = homeRepository.getPagingData(language = "en-us")
        .cachedIn(viewModelScope)

    private var _movie = MutableStateFlow(DetailUiState(isLoading = true))
    val movie = _movie.asStateFlow()

    fun actionHandler(action: HomeAction) {
        when (action) {
            is HomeAction.Init -> {}
            HomeAction.OnBackPressed -> {
                TODO()
            }

            HomeAction.PullToRefresh -> {
                TODO()
            }

            is HomeAction.Toggle -> {
                TODO()
            }

            is HomeAction.ShowSnackbar -> {
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
                                println("Snackbar Snackbar dismissed")
                            }
                        }
                    }
                }
            }

            is HomeAction.DeleteMove -> {
                viewModelScope.launch {
                    homeRepository.deleteMovie(action.movie.id)
                }
            }

            is HomeAction.LoadDetails -> {
                viewModelScope.launch {
                    homeRepository.getMovieDetail(action.movieId, "en-US")
                        .map { movieDetail -> DetailUiState(data = movieDetail, isLoading = false) }
                        .collect { state ->
                            _movie.update { state }
                        }
                }

            }

            is HomeAction.NavigateToDetailsScreen -> {

            }
        }
    }

}
package com.bajobozic.port.home.presentation

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.bajobozic.port.detail.presentation.DetailUiState
import com.bajobozic.port.home_component.domain.usecase.GetPagingDataUseCase
import com.bajobozic.port.storage.domain.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getPagingDataUseCase: GetPagingDataUseCase,
) : ViewModel() {
    val snackbarHostState = SnackbarHostState()

    //we should probably do something like this if we want to change param
    /*
    var _language = MutableStateFlow("en-US")
    val language: StateFlow<String> = _language
    */
    val homePagingData: Flow<PagingData<Movie>> = getPagingDataUseCase("en-US")

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
                                println("Snackbar, Snackbar dismissed")
                            }
                        }
                    }
                }
            }

            is HomeAction.DeleteMove -> {
                viewModelScope.launch {
                }
            }

            is HomeAction.NavigateToDetailsScreen -> {
                //no op, handled in navController
            }

            HomeAction.NavigateToSignInScreen -> {
                //no op, handled in navController
            }
        }
    }

}
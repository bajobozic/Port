package com.bajobozic.port.detail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bajobozic.port.network.domain.usecase.GetMovieDetailUseCase
import com.bajobozic.port.network.domain.usecase.GetMovieVideoUseCase
import com.bajobozic.port.shared_component.domain.onError
import com.bajobozic.port.shared_component.domain.onSuccess
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(
    val movieId: Int,
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val getMovieVideoUseCase: GetMovieVideoUseCase
) :
    ViewModel() {
    private var _movie = MutableStateFlow(DetailUiState(isLoading = true))
    val movie = _movie.asStateFlow()

    init {
        viewModelScope.launch {
            val movieDetail = async { getMovieDetailUseCase(movieId, "en-US") }
            val movieVideos = async { getMovieVideoUseCase("en-US", movieId) }
            val (movieDetailResult, videoResult) = movieDetail.await() to movieVideos.await()

            videoResult
                .onSuccess { videos ->
                    val last = videos.lastOrNull()
                    movieDetailResult.onSuccess { movieDetail ->
                        _movie.update {
                            DetailUiState(
                                data = movieDetail.copy(
                                    key = last?.key.orEmpty(),
                                    site = last?.site.orEmpty(),
                                    size = last?.size ?: 0
                                ),
                                isLoading = false,
                                error = ""
                            )
                        }
                    }.onError { error ->
                        _movie.update {
                            it.copy(isLoading = false)
                            return@launch
                        }

                    }
                        .onError { error ->
                            _movie.update {
                                it.copy(isLoading = false)
                            }

                        }
                }
        }
    }

    fun onEvent(event: DetailScreenEvent) {
        when (event) {
            is DetailScreenEvent.ToggleVideoFullscreen -> {
                _movie.update {
                    it.copy(isVideoFullscreen = event.toggleToFullScreen)
                }
            }

            is DetailScreenEvent.OpenMaps -> {
                // No-op
            }

            is DetailScreenEvent.ShowNotification -> {
                _movie.update { it.copy(notificationTitle = event.movieName) }
            }

            is DetailScreenEvent.ResetNotification -> {
                _movie.update { it.copy(notificationTitle = null) }
            }
        }
    }
}
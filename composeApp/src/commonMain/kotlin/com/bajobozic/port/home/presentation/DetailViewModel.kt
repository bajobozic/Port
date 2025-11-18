package com.bajobozic.port.home.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bajobozic.port.home.domain.repository.HomeRepository
import com.bajobozic.port.home.presentation.component.DetailScreenEvent
import com.bajobozic.port.home.presentation.component.DetailUiState
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val homeRepository: HomeRepository
) :
    ViewModel() {
    private val movieId = savedStateHandle.get<Int>("movieId")
        ?: throw IllegalArgumentException("movieId is required")
    private var _movie = MutableStateFlow(DetailUiState(isLoading = true))
    val movie = _movie.asStateFlow()

    init {
        viewModelScope.launch {
            val movieDetail = async { homeRepository.getMovieDetail(movieId, "en-US") }
            val movieVideos = async { homeRepository.getMovieVideo(movieId, "en-US") }
            val video = movieVideos.await().lastOrNull()
            _movie.update {
                DetailUiState(
                    data = movieDetail.await().copy(
                        key = video?.key.orEmpty(),
                        site = video?.site.orEmpty(),
                        size = video?.size ?: 0
                    ),
                    isLoading = false,
                    error = ""
                )
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
        }
    }
}
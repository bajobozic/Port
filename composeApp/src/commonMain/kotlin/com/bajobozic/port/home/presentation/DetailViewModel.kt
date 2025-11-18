package com.bajobozic.port.home.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bajobozic.port.home.domain.repository.HomeRepository
import com.bajobozic.port.home.presentation.component.DetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
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
            homeRepository.getMovieDetail(movieId, "en-US")
                .map { movieDetail -> DetailUiState(data = movieDetail, isLoading = false) }
                .collect { state ->
                    _movie.update { state }
                }
            val video = homeRepository.getMovieVideo(movieId, "en-US").firstOrNull()
            video?.let { movieVideo ->
                _movie.update { currentState ->
                    currentState.copy(
                        data = currentState.data.copy(
                            key = movieVideo.key,
                            site = movieVideo.site,
                            size = movieVideo.size
                        )
                    )
                }
            }
        }
    }
}
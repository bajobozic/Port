package com.bajobozic.port.home.presentation.component

import com.bajobozic.port.home.domain.model.MovieDetail


data class DetailUiState(
    val data: MovieDetail = MovieDetail(),
    val isLoading: Boolean = false,
    val error: String = ""
)

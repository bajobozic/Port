package com.bajobozic.port.home.presentation.component

import com.bajobozic.port.home.domain.model.Movie

data class HomeUiState(
    val data: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
)

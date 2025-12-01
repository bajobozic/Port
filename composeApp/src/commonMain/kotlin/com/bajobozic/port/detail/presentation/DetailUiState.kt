package com.bajobozic.port.detail.presentation

import com.bajobozic.port.network.domain.model.MovieDetail

data class DetailUiState(
    val data: MovieDetail = MovieDetail(),
    val isLoading: Boolean = false,
    val error: String = "",
    val isVideoFullscreen: Boolean = false,
    val notificationTitle: String? = null,
)
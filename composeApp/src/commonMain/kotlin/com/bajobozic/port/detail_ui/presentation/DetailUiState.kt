package com.bajobozic.port.detail_ui.presentation

import com.bajobozic.network.domain.model.MovieDetail


internal data class DetailUiState(
    val data: MovieDetail = MovieDetail(),
    val isLoading: Boolean = false,
    val error: String = "",
    val isVideoFullscreen: Boolean = false,
    val notificationTitle: String? = null,
)
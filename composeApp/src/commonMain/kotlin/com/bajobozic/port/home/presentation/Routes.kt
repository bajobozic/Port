package com.bajobozic.port.home.presentation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Routes {
    @Serializable
    data object Home : Routes

    @Serializable
    data class Details(val movieId: String) : Routes
}
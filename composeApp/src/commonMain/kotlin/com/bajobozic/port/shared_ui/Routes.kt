package com.bajobozic.port.shared_ui

import kotlinx.serialization.Serializable

sealed interface Routes {
    @Serializable
    data object Home : Routes

    @Serializable
    data object SignIn : Routes

    @Serializable
    data object Maps : Routes

    @Serializable
    data class Details(val movieId: Int) : Routes
}
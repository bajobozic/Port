package com.bajobozic.shared_ui

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Routes : NavKey {
    @Serializable
    data object Home : Routes

    @Serializable
    data object SignIn : Routes

    @Serializable
    data object Maps : Routes

    @Serializable
    data class Details(val movieId: Int) : Routes
}
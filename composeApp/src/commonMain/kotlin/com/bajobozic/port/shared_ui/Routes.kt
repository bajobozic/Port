package com.bajobozic.port.shared_ui

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Routes : NavKey {
    @Serializable
    data object Home : Routes, NavKey

    @Serializable
    data object SignIn : Routes, NavKey

    @Serializable
    data object Maps : Routes, NavKey

    @Serializable
    data class Details(val movieId: Int) : Routes, NavKey
}
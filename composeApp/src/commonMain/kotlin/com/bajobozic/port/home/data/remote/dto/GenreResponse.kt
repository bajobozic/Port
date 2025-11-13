package com.bajobozic.port.home.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class GenreResponse(val genres: List<GenreDto>)

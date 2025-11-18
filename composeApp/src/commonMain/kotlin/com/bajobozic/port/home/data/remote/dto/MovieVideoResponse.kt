package com.bajobozic.port.home.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MovieVideoResponse(val id: Int? = null, val results: List<MovieVideoDto>? = null)

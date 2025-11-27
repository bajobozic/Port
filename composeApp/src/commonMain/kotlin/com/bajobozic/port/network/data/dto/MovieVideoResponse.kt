package com.bajobozic.port.network.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class MovieVideoResponse(val id: Int? = null, val results: List<MovieVideoDto>? = null)

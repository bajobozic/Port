package com.bajobozic.network.data.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class MovieVideoResponse(
    val id: Int? = null,
    val results: List<MovieVideoDto>? = null
)

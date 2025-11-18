package com.bajobozic.port.home.data.remote.dto

import com.bajobozic.port.home.domain.model.MovieVideo
import kotlinx.serialization.Serializable

@Serializable
data class MovieVideoDto(
    val key: String? = null,
    val site: String? = null,
    val size: Int? = null
)

fun MovieVideoDto.toMovieVideo(): MovieVideo {
    return MovieVideo(
        key = this.key.orEmpty(),
        site = this.site.orEmpty(),
        size = this.size ?: 0
    )
}

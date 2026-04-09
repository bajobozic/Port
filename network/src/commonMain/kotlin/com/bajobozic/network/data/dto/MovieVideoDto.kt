package com.bajobozic.network.data.dto

import com.bajobozic.network.domain.model.MovieVideo
import kotlinx.serialization.Serializable

@Serializable
internal data class MovieVideoDto(
    val key: String? = null,
    val site: String? = null,
    val size: Int? = null
)

internal fun MovieVideoDto.toMovieVideo(): MovieVideo {
    return MovieVideo(
        key = this.key.orEmpty(),
        site = this.site.orEmpty(),
        size = this.size ?: 0
    )
}

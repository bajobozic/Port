package com.bajobozic.network.data.dto

import com.bajobozic.network.domain.model.Genre
import kotlinx.serialization.Serializable

@Serializable
internal data class GenreResponse(val genres: List<GenreDto>)

internal fun GenreResponse.toGenreList(): List<Genre> {
    return genres.map { genreDto ->
        Genre(
            id = genreDto.id,
            name = genreDto.name
        )
    }
}
package com.bajobozic.port.network.data.dto

import com.bajobozic.port.network.domain.model.Genre
import kotlinx.serialization.Serializable

@Serializable
data class GenreResponse(val genres: List<GenreDto>)

fun GenreResponse.toGenreList(): List<Genre> {
    return genres.map { genreDto -> Genre(id = genreDto.id, name = genreDto.name) }
}
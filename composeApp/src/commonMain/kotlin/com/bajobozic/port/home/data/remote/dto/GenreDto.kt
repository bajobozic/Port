package com.bajobozic.port.home.data.remote.dto

import com.bajobozic.port.home.data.locale.entity.GenreEntity
import kotlinx.serialization.Serializable

@Serializable
data class GenreDto(val id: Int = 0, val name: String = "")

fun GenreDto.toEntity(): GenreEntity {
    return GenreEntity(id, name)
}

fun GenreDto.toModel(): com.bajobozic.port.home.domain.model.Genre {
    return com.bajobozic.port.home.domain.model.Genre(id, name)
}

val EMPTY_GENRE_DTO = GenreDto()
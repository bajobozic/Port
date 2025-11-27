package com.bajobozic.port.network.data.dto

import com.bajobozic.port.storage.data.entity.GenreEntity
import com.bajobozic.port.storage.domain.model.Genre
import kotlinx.serialization.Serializable

@Serializable
data class GenreDto(val id: Int = 0, val name: String = "")

fun GenreDto.toEntity(): GenreEntity {
    return GenreEntity(id, name)
}

fun GenreDto.toModel(): Genre {
    return Genre(id, name)
}

val EMPTY_GENRE_DTO = GenreDto()
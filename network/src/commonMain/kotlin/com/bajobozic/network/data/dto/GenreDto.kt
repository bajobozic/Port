package com.bajobozic.network.data.dto


import com.bajobozic.network.domain.model.Genre
import kotlinx.serialization.Serializable

@Serializable
internal data class GenreDto(val id: Int = 0, val name: String = "")

//fun GenreDto.toEntity(): GenreEntity {
//    return GenreEntity(id, name)
//}

internal fun GenreDto.toModel(): Genre {
    return Genre(id, name)
}

internal val EMPTY_GENRE_DTO = GenreDto()
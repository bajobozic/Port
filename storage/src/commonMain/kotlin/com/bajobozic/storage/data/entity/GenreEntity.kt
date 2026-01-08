package com.bajobozic.storage.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bajobozic.storage.domain.model.Genre

@Entity(tableName = "genres")
data class GenreEntity(
    @PrimaryKey
    @ColumnInfo(name = "genre_id")
    val id: Int,
    val name: String
)

fun GenreEntity.toModel(): Genre {
    return Genre(id, name)
}
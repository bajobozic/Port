package com.bajobozic.port.home.data.locale.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "moviegenrecrossref", primaryKeys = ["movie_id", "genre_id"])
data class MovieGenreCrossRef(
    @ColumnInfo(name = "movie_id")
    val movieId: Int,
    @ColumnInfo(name = "genre_id")
    val genreId: Int
)

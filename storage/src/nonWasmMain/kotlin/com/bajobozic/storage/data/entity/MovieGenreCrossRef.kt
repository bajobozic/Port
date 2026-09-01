package com.bajobozic.storage.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "moviegenrecrossref", primaryKeys = ["movie_id", "genre_id"], indices = [
        Index(value = ["genre_id"])
    ]
)
internal data class MovieGenreCrossRef(
    @ColumnInfo(name = "movie_id")
    val movieId: Int,
    @ColumnInfo(name = "genre_id")
    val genreId: Int
)

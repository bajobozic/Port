package com.bajobozic.storage.data.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

internal data class GenreWithMovies(
    @Embedded
    val genre: GenreEntity,
    @Relation(
        parentColumn = "genre_id",
        entityColumn = "movie_id",
        associateBy = Junction(MovieGenreCrossRef::class)
    )
    val movies: List<MovieEntity>
)

package com.bajobozic.port.home.data.locale.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import core.data.local.entity.MovieEntity
import core.data.local.entity.MovieGenreCrossRef

data class GenreWithMovies(
    @Embedded
    val genre: GenreEntity,
    @Relation(
        parentColumn = "genre_id",
        entityColumn = "movie_id",
        associateBy = Junction(MovieGenreCrossRef::class)
    )
    val movies: List<MovieEntity>
)

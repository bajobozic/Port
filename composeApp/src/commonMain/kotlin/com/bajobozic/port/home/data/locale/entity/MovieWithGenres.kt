package com.bajobozic.port.home.data.locale.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.bajobozic.port.home.domain.model.GetMoviesWithGenres
import com.bajobozic.port.home.domain.model.Movie

data class MovieWithGenres(
    @Embedded
    val movie: MovieEntity,
    @Relation(
        parentColumn = "movie_id",
        entityColumn = "genre_id",
        associateBy = Junction(MovieGenreCrossRef::class)
    )
    val genres: List<GenreEntity>
) : GetMoviesWithGenres

fun MovieWithGenres.toModel(): Movie {
    return Movie(
        id = movie.id,
        posterPath = movie.backdropPath,
        title = movie.title,
        overview = movie.overview,
        genreIds = genres.map { it.toModel() },
        releaseDate = movie.releaseDate
    )
}

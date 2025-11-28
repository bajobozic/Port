package com.bajobozic.port.storage.data.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.bajobozic.port.storage.domain.model.GetMovieWithGenres
import com.bajobozic.port.storage.domain.model.Movie

data class MovieWithGenres(
    @Embedded
    val movie: MovieEntity,
    @Relation(
        parentColumn = "movie_id",
        entityColumn = "genre_id",
        associateBy = Junction(MovieGenreCrossRef::class)
    )
    val genres: List<GenreEntity>
) : GetMovieWithGenres {
    override fun toModel(): Movie {
        return Movie(
            id = movie.id,
            posterPath = movie.backdropPath,
            title = movie.title,
            overview = movie.overview,
            genreIds = genres.map { it.toModel() },
            releaseDate = movie.releaseDate,
            previousPage = movie.previousPage ?: 0,
            currentPage = movie.currentPage ?: 0,
            nextPage = movie.nextPage ?: 0,
            adult = movie.adult,
            backdropPath = movie.backdropPath,
            originalLanguage = movie.originalLanguage,
            popularity = movie.popularity,
            video = movie.video,
            voteAverage = movie.voteAverage,
            voteCount = movie.voteCount,
            originalTitle = movie.originalTitle
        )
    }
}

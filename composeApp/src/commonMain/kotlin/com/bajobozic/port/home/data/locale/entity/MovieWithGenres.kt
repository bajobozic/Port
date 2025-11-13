package home.data.local.db

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.bajobozic.port.home.data.locale.entity.GenreEntity
import core.data.local.entity.MovieEntity
import core.data.local.entity.MovieGenreCrossRef
import com.bajobozic.port.home.data.locale.entity.toModel
import com.bajobozic.port.home.domain.model.GetMoviesWithGenres
import com.bajobozic.port.home.domain.model.Movie
import com.bajobozic.port.home.domain.model.MovieDetail

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
        posterPath = movie.posterPath,
        title = movie.title,
        overview = movie.overview,
        genreIds = genres.map { it.toModel() },
        releaseDate = movie.releaseDate
    )
}

fun MovieWithGenres.toModelDetail(): MovieDetail {
    return MovieDetail(
        adult = movie.adult,
        backdropPath = movie.backdropPath,
        genreIds = genres.map { it.toModel() },
        originalLanguage = movie.originalLanguage,
        overview = movie.overview,
        popularity = movie.popularity,
        posterPath = movie.posterPath,
        releaseDate = movie.releaseDate,
        title = movie.title,
        video = movie.video,
        voteAverage = movie.voteAverage,
        voteCount = movie.voteCount
    )
}

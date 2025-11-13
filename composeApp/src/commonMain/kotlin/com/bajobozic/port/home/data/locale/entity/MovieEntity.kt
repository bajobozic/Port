package core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bajobozic.port.home.domain.model.Movie
import kotlinx.datetime.LocalDate

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    val id: Int,
    val adult: Boolean,
    val backdropPath: String,
    //add to  Genres entity MovieEntity id
    //to be able to have onetomany relation
    /* @SerialName(value = "genre_ids")
     val genreIds: List<Int>,*/
    @ColumnInfo(name = "original_language")
    val originalLanguage: String,
    @ColumnInfo(name = "original_title", defaultValue = "")
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    @ColumnInfo(name = "poster_path", defaultValue = "")
    val posterPath: String,
    @ColumnInfo(name = "release_date")
    val releaseDate: LocalDate,
    val title: String,
    val video: Boolean,
    @ColumnInfo(name = "vote_average", defaultValue = "0.0")
    val voteAverage: Double,
    @ColumnInfo(name = "vote_count", defaultValue = "0")
    val voteCount: Int,
    @ColumnInfo(name = "previous_page")
    val previousPage: Int?,
    @ColumnInfo(name = "current_page")
    val currentPage: Int?,
    @ColumnInfo(name = "next_page")
    val nextPage: Int?,
)

fun MovieEntity.toModel(): Movie {
    return Movie(
        genreIds = emptyList(),
        id = id,
        overview = overview,
        posterPath = posterPath,
        title = title
    )
}

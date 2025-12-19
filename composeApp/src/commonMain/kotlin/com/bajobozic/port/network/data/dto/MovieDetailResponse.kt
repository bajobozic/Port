package com.bajobozic.port.network.data.dto

import com.bajobozic.port.network.domain.model.MovieDetail
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailResponse(
    @SerialName(value = "id")
    val id: Int,
    @SerialName(value = "adult")
    val adult: Boolean,
    @SerialName(value = "backdrop_path")
    val backdropPath: String?,
    @SerialName(value = "genres")
    val genreIds: List<GenreDto> = emptyList(),
    @SerialName(value = "original_language")
    val originalLanguage: String?,
    @SerialName(value = "original_title")
    val originalTitle: String?,
    @SerialName(value = "overview")
    val overview: String?,
    @SerialName(value = "popularity")
    val popularity: Double,
    @SerialName(value = "poster_path")
    val posterPath: String?,
    @SerialName(value = "release_date")
    val releaseDate: String,
    @SerialName(value = "title")
    val title: String?,
    @SerialName(value = "video")
    val video: Boolean,
    @SerialName(value = "vote_average")
    val voteAverage: Double,
    @SerialName(value = "vote_count")
    val voteCount: Int
)

fun MovieDetailResponse.toMovieDetail(): MovieDetail {
    return MovieDetail(
        id = id,
        adult = adult,
        backdropPath = backdropPath.orEmpty(),
        genreIds = genreIds.map { it.toModel() },
        originalLanguage = originalLanguage.orEmpty(),
        overview = overview.orEmpty(),
        popularity = popularity,
        posterPath = posterPath.orEmpty(),
        releaseDate = if (releaseDate.isBlank()) LocalDate(
            1977,
            1,
            1
        ) else LocalDate.parse(input = releaseDate),
        title = title.orEmpty(),
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount
    )
}

fun MovieDetailResponse.toMoviesResponse(): MoviesResponse {
    return MoviesResponse(
        adult = adult,
        backdropPath = backdropPath,
        genreIds = genreIds.map { it.id },
        id = id,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        releaseDate = releaseDate,
        title = title,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount
    )
}
package com.bajobozic.port.home.data.remote.dto

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailResponse(
    val adult: Boolean,
    @SerialName(value = "backdrop_path")
    val backdropPath: String?,
    @SerialName(value = "genres")
    val genreIds: List<GenreDto> = emptyList(),
    val id: Int,
    @SerialName(value = "original_language")
    val originalLanguage: String?,
    @SerialName(value = "original_title")
    val originalTitle: String?,
    val overview: String?,
    val popularity: Double,
    @SerialName(value = "poster_path")
    val posterPath: String?,
    @SerialName(value = "release_date")
    val releaseDate: LocalDate,
    val title: String?,
    val video: Boolean,
    @SerialName(value = "vote_average")
    val voteAverage: Double,
    @SerialName(value = "vote_count")
    val voteCount: Int
)

fun MovieDetailResponse.toMovieDetail(): com.bajobozic.port.home.domain.model.MovieDetail {
    return com.bajobozic.port.home.domain.model.MovieDetail(
        adult = adult,
        backdropPath = backdropPath.orEmpty(),
        genreIds = genreIds.map { it.toModel() },
        originalLanguage = originalLanguage.orEmpty(),
        overview = overview.orEmpty(),
        popularity = popularity,
        posterPath = posterPath.orEmpty(),
        releaseDate = releaseDate,
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
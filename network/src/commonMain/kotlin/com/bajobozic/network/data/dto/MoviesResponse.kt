package com.bajobozic.network.data.dto

import com.bajobozic.network.domain.model.Genre
import com.bajobozic.network.domain.model.MovieDetail

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MoviesResponse(
    @SerialName(value = "id")
    val id: Int,
    @SerialName(value = "adult")
    val adult: Boolean,
    @SerialName(value = "backdrop_path")
    val backdropPath: String?,
    @SerialName(value = "genre_ids")
    val genreIds: List<Int> = emptyList(),
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
) {
    var previousPage: Int? = null
    var currentPage: Int = 0
    var nextPage: Int = 0
}

internal fun MoviesResponse.initKeys(
    page: Int
) = this.apply {
    this.previousPage = if (page <= 1) null else page - 1
    this.currentPage = if (page < 1) 1 else page
    this.nextPage = if (page < 1) 1 else page + 1
}

internal fun MoviesResponse.toMovieDetail(): MovieDetail {
    return MovieDetail(
        id = id,
        adult = adult,
        backdropPath = backdropPath.orEmpty(),
        genreIds = genreIds.map { Genre(id = it, name = "") },
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
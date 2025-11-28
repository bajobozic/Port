package com.bajobozic.port.network.data.dto

import com.bajobozic.port.network.domain.model.MovieDetail
import com.bajobozic.port.storage.domain.model.Genre
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MoviesResponse(
    val adult: Boolean,
    @SerialName(value = "backdrop_path")
    val backdropPath: String?,
    @SerialName(value = "genre_ids")
    val genreIds: List<Int> = emptyList(),
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

fun MoviesResponse.toMovieDetail(): MovieDetail {
    return MovieDetail(
        id = id,
        adult = adult,
        backdropPath = backdropPath.orEmpty(),
        genreIds = genreIds.map { Genre(id = it, name = "") },
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
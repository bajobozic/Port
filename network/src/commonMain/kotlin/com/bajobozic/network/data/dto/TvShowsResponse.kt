package com.bajobozic.network.data.dto

import com.bajobozic.network.domain.model.Genre
import com.bajobozic.network.domain.model.TvShowDetail
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class TvShowsResponse(
    @SerialName(value = "id")
    val id: Int,
    @SerialName(value = "backdrop_path")
    val backdropPath: String?,
    @SerialName(value = "genre_ids")
    val genreIds: List<Int> = emptyList(),
    @SerialName(value = "original_language")
    val originalLanguage: String?,
    @SerialName(value = "original_name")
    val originalName: String?,
    @SerialName(value = "overview")
    val overview: String?,
    @SerialName(value = "popularity")
    val popularity: Double,
    @SerialName(value = "poster_path")
    val posterPath: String?,
    @SerialName(value = "first_air_date")
    val firstAirDate: String,
    @SerialName(value = "name")
    val name: String?,
    @SerialName(value = "vote_average")
    val voteAverage: Double,
    @SerialName(value = "vote_count")
    val voteCount: Int
) {
    var previousPage: Int? = null
    var currentPage: Int = 0
    var nextPage: Int = 0
}

internal fun TvShowsResponse.initKeys(
    page: Int
) = this.apply {
    this.previousPage = if (page <= 1) null else page - 1
    this.currentPage = if (page < 1) 1 else page
    this.nextPage = if (page < 1) 1 else page + 1
}

internal fun TvShowsResponse.toTvShowDetail(): TvShowDetail {
    return TvShowDetail(
        id = id,
        backdropPath = backdropPath.orEmpty(),
        genreIds = genreIds.map { Genre(id = it, name = "") },
        originalLanguage = originalLanguage.orEmpty(),
        overview = overview.orEmpty(),
        popularity = popularity,
        posterPath = posterPath.orEmpty(),
        firstAirDate = if (firstAirDate.isBlank()) LocalDate(
            1977,
            1,
            1
        ) else LocalDate.parse(input = firstAirDate),
        name = name.orEmpty(),
        voteAverage = voteAverage,
        voteCount = voteCount,
        originalName = originalName.orEmpty()
    )
}

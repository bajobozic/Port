package com.bajobozic.network.domain.model

import kotlinx.datetime.LocalDate

data class TvShowDetail(
    val id: Int = 0,
    val backdropPath: String = "",
    val genreIds: List<Genre> = emptyList(),
    val originalLanguage: String = "",
    val overview: String = "",
    val popularity: Double = 0.0,
    val posterPath: String = "",
    val firstAirDate: LocalDate = LocalDate(1977, 1, 1),
    val name: String = "",
    val voteAverage: Double = 0.0,
    val voteCount: Int = 0,
    val originalName: String = ""
) {
    var currentPage: Int = 0
}

internal fun TvShowDetail.initKeys(
    page: Int
) = this.apply {
    this.currentPage = if (page < 1) 1 else page
}

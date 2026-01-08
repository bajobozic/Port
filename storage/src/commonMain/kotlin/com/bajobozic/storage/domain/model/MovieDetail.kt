package com.bajobozic.storage.domain.model

import kotlinx.datetime.LocalDate

data class MovieDetail(
    val id: Int = 0,
    val adult: Boolean = false,
    val backdropPath: String = "",
    val genreIds: List<Genre> = emptyList(),
    val originalLanguage: String = "",
    val overview: String = "",
    val popularity: Double = 0.0,
    val posterPath: String = "",
    val releaseDate: LocalDate = LocalDate(1977, 1, 1),
    val title: String = "",
    val video: Boolean = false,
    val voteAverage: Double = 0.0,
    val voteCount: Int = 0,
    val key: String = "",
    val site: String = "",
    val size: Int = 0
) {
    var currentPage: Int = 0
}

internal fun MovieDetail.initKeys(
    page: Int
) = this.apply {
    this.currentPage = if (page < 1) 1 else page
}
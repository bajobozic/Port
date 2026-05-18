package com.bajobozic.storage.domain.model

import kotlinx.datetime.LocalDate

data class TvShow(
    val genreIds: List<Genre> = emptyList(),
    val id: Int = 0,
    val overview: String = "",
    val posterPath: String = "",
    val name: String = "",
    val firstAirDate: LocalDate = LocalDate(1977, 1, 1),
    val currentPage: Int = 0,
    val backdropPath: String = "",
    val originalLanguage: String = "",
    val popularity: Double = 0.0,
    val voteAverage: Double = 0.0,
    val voteCount: Int = 0,
    val originalName: String = ""
)

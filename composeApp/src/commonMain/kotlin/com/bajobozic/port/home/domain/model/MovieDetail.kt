package com.bajobozic.port.home.domain.model

import kotlinx.datetime.LocalDate

data class MovieDetail(
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
)

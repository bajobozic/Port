package com.bajobozic.port.storage.domain.model

import kotlinx.datetime.LocalDate

data class Movie(
    val genreIds: List<Genre> = emptyList(),
    val id: Int = 0,
    val overview: String = "",
    val posterPath: String = "",
    val title: String = "",
    val releaseDate: LocalDate = LocalDate(1977, 1, 1),
    val currentPage: Int = 0,
    val adult: Boolean = false,
    val backdropPath: String = "",
    val originalLanguage: String = "",
    val popularity: Double = 0.0,
    val video: Boolean = false,
    val voteAverage: Double = 0.0,
    val voteCount: Int = 0,
    val originalTitle: String = ""
)

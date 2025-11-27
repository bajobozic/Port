package com.bajobozic.port.storage.domain.model

import kotlinx.datetime.LocalDate

data class Movie(
    val genreIds: List<Genre> = emptyList(),
    val id: Int = 0,
    val overview: String = "",
    val posterPath: String = "",
    val title: String = "",
    val releaseDate: LocalDate = LocalDate(1977, 1, 1),
    val previousPage: Int = 0,
    val currentPage: Int = 0,
    val nextPage: Int = 0,
)

val EMPTY_MOVIE = Movie()

package com.bajobozic.port.home.domain.model

import kotlinx.datetime.LocalDate

data class Movie(
    val genreIds: List<Genre> = emptyList(),
    val id: Int = 0,
    val overview: String = "",
    val posterPath: String = "",
    val title: String = "",
    val releaseDate: LocalDate = LocalDate(1977, 1, 1)
)

val EMPTY_MOVIE = Movie()

package com.bajobozic.network.domain.model


data class PopularMovies(
    val page: Int,
    val movies: List<MovieDetail> = emptyList(),
    val totalPages: Int,
    val totalResults: Int
)

fun PopularMovies.initRemoteKeys() =
    this.apply { movies.forEach { movie -> movie.initKeys(this.page) } }

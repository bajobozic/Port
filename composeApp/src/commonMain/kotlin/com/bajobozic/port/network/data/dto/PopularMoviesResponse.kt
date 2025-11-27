package com.bajobozic.port.network.data.dto

import com.bajobozic.port.network.domain.model.PopularMovies
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PopularMoviesResponse(
    val page: Int,
    @SerialName(value = "results")
    val movies: List<MoviesResponse> = emptyList(),
    @SerialName(value = "total_pages")
    val totalPages: Int,
    @SerialName(value = "total_results")
    val totalResults: Int
)

fun PopularMoviesResponse.initRemoteKeys() =
    this.apply { movies.forEach { movie -> movie.initKeys(this.page) } }

fun PopularMoviesResponse.toModel() = PopularMovies(
    page = page,
    movies = movies.map { it.toMovieDetail() },
    totalPages = totalPages,
    totalResults = totalResults
)
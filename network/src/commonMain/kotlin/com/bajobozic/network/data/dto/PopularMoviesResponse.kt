package com.bajobozic.network.data.dto

import com.bajobozic.network.domain.model.PopularMovies
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class PopularMoviesResponse(
    val page: Int,
    @SerialName(value = "results")
    val movies: List<com.bajobozic.network.data.dto.MoviesResponse> = emptyList(),
    @SerialName(value = "total_pages")
    val totalPages: Int,
    @SerialName(value = "total_results")
    val totalResults: Int
)

internal fun PopularMoviesResponse.initRemoteKeys() =
    this.apply { movies.forEach { movie -> movie.initKeys(this.page) } }

internal fun PopularMoviesResponse.toModel() =
    PopularMovies(
        page = page,
        movies = movies.map { it.toMovieDetail() },
        totalPages = totalPages,
        totalResults = totalResults
    )
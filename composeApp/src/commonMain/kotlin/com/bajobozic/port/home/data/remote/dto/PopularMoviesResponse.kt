package com.bajobozic.port.home.data.remote.dto

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
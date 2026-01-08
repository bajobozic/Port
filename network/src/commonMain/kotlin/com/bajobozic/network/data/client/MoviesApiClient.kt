package com.bajobozic.network.data.client

import com.bajobozic.network.data.dto.GenreResponse
import com.bajobozic.network.data.dto.MovieDetailResponse
import com.bajobozic.network.data.dto.MovieVideoResponse
import com.bajobozic.network.data.dto.PopularMoviesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

internal class MoviesApiClient(private val client: HttpClient) :
    ApiClient {
    override suspend fun getMovies(language: String, page: Int): PopularMoviesResponse {
        return client.get(urlString = "https://api.themoviedb.org/3/movie/popular") {
            parameter("language", language)
            parameter("page", page)
        }.body<PopularMoviesResponse>()
    }

    override suspend fun getGenres(): GenreResponse {
        return client.get(urlString = "https://api.themoviedb.org/3/genre/movie/list")
            .body<GenreResponse>()
    }

    override suspend fun getMovie(movieId: Int, language: String): MovieDetailResponse {
        return client.get(urlString = "https://api.themoviedb.org/3/movie/${movieId}") {
            parameter("language", language)
        }.body<MovieDetailResponse>()
    }

    override suspend fun getMovieVideos(
        movieId: Int,
        language: String
    ): MovieVideoResponse {
        return client.get(urlString = "https://api.themoviedb.org/3/movie/${movieId}/videos") {
            parameter("language", language)
        }.body<MovieVideoResponse>()
    }
}
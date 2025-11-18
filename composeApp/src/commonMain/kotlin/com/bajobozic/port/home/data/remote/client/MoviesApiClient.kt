package com.bajobozic.port.home.data.remote.client

import com.bajobozic.port.home.data.remote.dto.GenreResponse
import com.bajobozic.port.home.data.remote.dto.MovieVideoResponse
import com.bajobozic.port.home.data.remote.dto.MoviesResponse
import com.bajobozic.port.home.data.remote.dto.PopularMoviesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class MoviesApiClient(private val client: HttpClient) : ApiClient {
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

    override suspend fun getMovie(movieId: Int, language: String): MoviesResponse {
        return client.get(urlString = "https://api.themoviedb.org/3/movie/${movieId}") {
            parameter("language", language)
        }.body<MoviesResponse>()
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
package com.bajobozic.port.home.data.remote.client

import com.bajobozic.port.home.data.remote.dto.GenreResponse
import com.bajobozic.port.home.data.remote.dto.MoviesResponse
import com.bajobozic.port.home.data.remote.dto.PopularMoviesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class MoviesApiClient(private val client: HttpClient) : ApiClient {
    override suspend fun getMovies(language: String, page: Int): PopularMoviesResponse? {
        val response = try {
            client.get(urlString = "https://api.themoviedb.org/3/movie/popular") {
                parameter("language", language)
                parameter("page", page)
            }
        } catch (t: Throwable) {
            null
        }
        return response?.body<PopularMoviesResponse>()
    }

    override suspend fun getGenres(): GenreResponse? {
        val response = try {
            client.get(urlString = "https://api.themoviedb.org/3/genre/movie/list")
        } catch (t: Throwable) {
            null
        }
        return response?.body<GenreResponse>()
    }

    override suspend fun getMovie(movieId: Int, language: String): MoviesResponse? {
        val response = try {
            client.get(urlString = "https://api.themoviedb.org/3/movie/{movie_id}") {
                parameter("language", language)
            }
        } catch (t: Throwable) {
            null
        }
        return response?.body<MoviesResponse>()
    }
}
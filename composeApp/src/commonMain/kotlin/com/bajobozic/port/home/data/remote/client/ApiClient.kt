package com.bajobozic.port.home.data.remote.client

import com.bajobozic.port.home.data.remote.dto.GenreResponse
import com.bajobozic.port.home.data.remote.dto.MovieVideoResponse
import com.bajobozic.port.home.data.remote.dto.MoviesResponse
import com.bajobozic.port.home.data.remote.dto.PopularMoviesResponse

interface ApiClient {
    suspend fun getMovies(language: String, page: Int): PopularMoviesResponse
    suspend fun getGenres(): GenreResponse
    suspend fun getMovie(movieId: Int, language: String): MoviesResponse
    suspend fun getMovieVideos(movieId: Int, language: String): MovieVideoResponse
}

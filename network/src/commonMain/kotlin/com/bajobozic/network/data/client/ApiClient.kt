package com.bajobozic.network.data.client

import com.bajobozic.network.data.dto.GenreResponse
import com.bajobozic.network.data.dto.MovieDetailResponse
import com.bajobozic.network.data.dto.MovieVideoResponse
import com.bajobozic.network.data.dto.PopularMoviesResponse

internal interface ApiClient {
    suspend fun getMovies(
        language: String,
        page: Int
    ): PopularMoviesResponse

    suspend fun getGenres(): GenreResponse
    suspend fun getMovie(
        movieId: Int,
        language: String
    ): MovieDetailResponse

    suspend fun getMovieVideos(
        movieId: Int,
        language: String
    ): MovieVideoResponse
}

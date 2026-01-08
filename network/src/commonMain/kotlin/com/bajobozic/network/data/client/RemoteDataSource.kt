package com.bajobozic.network.data.client

import com.bajobozic.network.data.dto.GenreDto
import com.bajobozic.network.data.dto.GenreResponse
import com.bajobozic.network.data.dto.MovieDetailResponse
import com.bajobozic.network.data.dto.MovieGenresIds
import com.bajobozic.network.data.dto.MovieVideoDto
import com.bajobozic.network.data.dto.MoviesResponse
import com.bajobozic.network.data.dto.PopularMoviesResponse

internal interface RemoteDataSource {
    suspend fun getMovies(language: String, page: Int): PopularMoviesResponse

    suspend fun getGenres(language: String): GenreResponse

    suspend fun getMovie(language: String, movieId: Int): MovieDetailResponse

    suspend fun getMovieGenresAndIds(
        language: String,
        movieId: Int
    ): MovieGenresIds<MoviesResponse, GenreDto, Int>

    suspend fun getMovieVideos(language: String, id: Int): List<MovieVideoDto>
}
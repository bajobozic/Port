package com.bajobozic.port.home.data.remote.client

import com.bajobozic.port.home.data.remote.dto.GenreDto
import com.bajobozic.port.home.data.remote.dto.GenreResponse
import com.bajobozic.port.home.data.remote.dto.ManyToManyDto
import com.bajobozic.port.home.data.remote.dto.MoviesResponse
import com.bajobozic.port.home.data.remote.dto.PopularMoviesResponse

interface HomeRemoteDataSource {
    suspend fun getMovies(language: String, page: Int): PopularMoviesResponse

    suspend fun getGenres(language: String): GenreResponse

    suspend fun getMovie(language: String, movieId: Int): MoviesResponse

    suspend fun getMovieWithRelationships(
        language: String,
        movieId: Int
    ): ManyToManyDto<MoviesResponse, GenreDto, Int>
}
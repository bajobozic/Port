package com.bajobozic.network.domain.repository

import com.bajobozic.network.data.dto.GenreDto
import com.bajobozic.network.data.dto.MovieGenresIds
import com.bajobozic.network.data.dto.MoviesResponse
import com.bajobozic.network.domain.model.Genre
import com.bajobozic.network.domain.model.MovieDetail
import com.bajobozic.network.domain.model.MovieVideo
import com.bajobozic.network.domain.model.PopularMovies
import com.bajobozic.shared_component.BaseError
import com.bajobozic.shared_component.Outcome


internal interface NetworkRepository {
    suspend fun getMovies(language: String, page: Int): Outcome<PopularMovies, BaseError>
    suspend fun getGenres(language: String): Outcome<List<Genre>, BaseError>
    suspend fun getMovie(
        language: String,
        movieId: Int
    ): Outcome<MovieDetail, BaseError>

    suspend fun getMovieWithRelationships(
        language: String,
        movieId: Int
    ): Outcome<MovieGenresIds<MoviesResponse, GenreDto, Int>, BaseError>

    suspend fun getMovieDetail(
        movieId: Int,
        language: String
    ): Outcome<MovieDetail, BaseError>

    suspend fun getMovieVideos(
        language: String,
        id: Int
    ): Outcome<List<MovieVideo>, BaseError>
}
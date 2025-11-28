package com.bajobozic.port.network.domain.repository

import com.bajobozic.port.network.data.dto.GenreDto
import com.bajobozic.port.network.data.dto.MovieGenresIds
import com.bajobozic.port.network.data.dto.MoviesResponse
import com.bajobozic.port.network.domain.model.Genre
import com.bajobozic.port.network.domain.model.MovieDetail
import com.bajobozic.port.network.domain.model.MovieVideo
import com.bajobozic.port.network.domain.model.PopularMovies
import com.bajobozic.port.shared_component.domain.BaseError
import com.bajobozic.port.shared_component.domain.Outcome

internal interface NetworkRepository {
    suspend fun getMovies(language: String, page: Int): Outcome<PopularMovies, BaseError>
    suspend fun getGenres(language: String): Outcome<List<Genre>, BaseError>
    suspend fun getMovie(language: String, movieId: Int): Outcome<MovieDetail, BaseError>
    suspend fun getMovieWithRelationships(
        language: String,
        movieId: Int
    ): Outcome<MovieGenresIds<MoviesResponse, GenreDto, Int>, BaseError>

    suspend fun getMovieDetail(movieId: Int, language: String): Outcome<MovieDetail, BaseError>
    suspend fun getMovieVideos(language: String, id: Int): Outcome<List<MovieVideo>, BaseError>
}
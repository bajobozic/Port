package com.bajobozic.port.home.data.remote.client

import com.bajobozic.port.home.data.remote.dto.GenreDto
import com.bajobozic.port.home.data.remote.dto.GenreResponse
import com.bajobozic.port.home.data.remote.dto.ManyToManyDto
import com.bajobozic.port.home.data.remote.dto.MovieDetailResponse
import com.bajobozic.port.home.data.remote.dto.MovieVideoDto
import com.bajobozic.port.home.data.remote.dto.MoviesResponse
import com.bajobozic.port.home.data.remote.dto.PopularMoviesResponse
import com.bajobozic.port.home.data.remote.dto.initKeys
import com.bajobozic.port.home.data.remote.dto.toMoviesResponse

internal class HomeRemoteDataSourceImpl(private val apiClient: ApiClient) :
    HomeRemoteDataSource {
    override suspend fun getMovies(language: String, page: Int): PopularMoviesResponse {
        val moviesResponse = apiClient.getMovies(language, page)
        moviesResponse.movies.forEach { it.initKeys(page) }
        return moviesResponse
    }

    override suspend fun getGenres(language: String): GenreResponse {
        return apiClient.getGenres()
    }

    override suspend fun getMovie(language: String, movieId: Int): MovieDetailResponse {
        return apiClient.getMovie(movieId, language)
    }

    override suspend fun getMovieWithRelationships(
        language: String,
        movieId: Int
    ): ManyToManyDto<MoviesResponse, GenreDto, Int> {
        val movie = apiClient.getMovie(movieId, language).toMoviesResponse()
        val genres = apiClient.getGenres().genres
        val list = listOf(movie).map { it.genreIds }
        return ManyToManyDto(listOf(movie), genres, list)
    }

    override suspend fun getMovieVideos(
        language: String,
        id: Int
    ): List<MovieVideoDto> {
        return apiClient.getMovieVideos(id, language).results.orEmpty()
    }
}






package com.bajobozic.port.home.data.remote.client

import com.bajobozic.port.home.data.remote.dto.GenreDto
import com.bajobozic.port.home.data.remote.dto.GenreResponse
import com.bajobozic.port.home.data.remote.dto.ManyToManyDto
import com.bajobozic.port.home.data.remote.dto.MoviesResponse
import com.bajobozic.port.home.data.remote.dto.PopularMoviesResponse
import com.bajobozic.port.home.data.remote.dto.initKeys

internal class HomeRemoteDataSourceImpl(private val apiClient: ApiClient) :
    HomeRemoteDataSource {
    override suspend fun getMovies(language: String, page: Int): PopularMoviesResponse {
        val moviesResponse = apiClient.getMovies(language, page)
        moviesResponse?.movies?.forEach { it.initKeys(page) }
        return moviesResponse ?: PopularMoviesResponse(
            page = 0,
            movies = emptyList(),
            totalPages = 0,
            totalResults = 0
        )
    }

    override suspend fun getGenres(language: String): GenreResponse {
        return apiClient.getGenres() ?: GenreResponse(genres = emptyList())
    }

    override suspend fun getMovie(language: String, movieId: Int): MoviesResponse {
        return apiClient.getMovie(movieId, language)!!
    }

    override suspend fun getMovieWithRelationships(
        language: String,
        movieId: Int
    ): ManyToManyDto<MoviesResponse, GenreDto, Int> {
        val movie = apiClient.getMovie(movieId, language)
        val genres = apiClient.getGenres()?.genres
        if (movie == null || genres == null)
            return ManyToManyDto(emptyList(), emptyList(), listOf(emptyList()))
        val list = listOf(movie).map { it.genreIds }
        return ManyToManyDto(listOf(movie), genres, list)
    }
}






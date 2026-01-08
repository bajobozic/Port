package com.bajobozic.network.data.client

import com.bajobozic.network.data.dto.GenreDto
import com.bajobozic.network.data.dto.GenreResponse
import com.bajobozic.network.data.dto.MovieDetailResponse
import com.bajobozic.network.data.dto.MovieGenresIds
import com.bajobozic.network.data.dto.MovieVideoDto
import com.bajobozic.network.data.dto.MoviesResponse
import com.bajobozic.network.data.dto.PopularMoviesResponse
import com.bajobozic.network.data.dto.initKeys
import com.bajobozic.network.data.dto.toMoviesResponse

internal class RemoteDataSourceImpl(private val apiClient: ApiClient) :
    RemoteDataSource {
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

    override suspend fun getMovieGenresAndIds(
        language: String,
        movieId: Int
    ): MovieGenresIds<MoviesResponse, GenreDto, Int> {
        val movie = apiClient.getMovie(movieId, language).toMoviesResponse()
        val genres = apiClient.getGenres().genres
        val list = listOf(movie).map { it.genreIds }
        return MovieGenresIds(
            listOf(movie),
            genres,
            list
        )
    }

    override suspend fun getMovieVideos(
        language: String,
        id: Int
    ): List<MovieVideoDto> {
        return apiClient.getMovieVideos(id, language).results.orEmpty()
    }
}






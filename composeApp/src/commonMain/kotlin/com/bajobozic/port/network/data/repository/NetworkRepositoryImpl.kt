package com.bajobozic.port.network.data.repository

import com.bajobozic.port.network.data.client.RemoteDataSource
import com.bajobozic.port.network.data.dto.GenreDto
import com.bajobozic.port.network.data.dto.MovieGenresIds
import com.bajobozic.port.network.data.dto.MoviesResponse
import com.bajobozic.port.network.data.dto.toGenreList
import com.bajobozic.port.network.data.dto.toModel
import com.bajobozic.port.network.data.dto.toMovieDetail
import com.bajobozic.port.network.data.dto.toMovieVideo
import com.bajobozic.port.network.data.util.NetworkErrorHandler
import com.bajobozic.port.network.domain.model.Genre
import com.bajobozic.port.network.domain.model.MovieDetail
import com.bajobozic.port.network.domain.model.MovieVideo
import com.bajobozic.port.network.domain.model.PopularMovies
import com.bajobozic.port.network.domain.repository.NetworkRepository
import com.bajobozic.port.shared_component.domain.BaseError
import com.bajobozic.port.shared_component.domain.Outcome

internal class NetworkRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val networkErrorHandler: NetworkErrorHandler
) : NetworkRepository {
    override suspend fun getMovies(
        language: String,
        page: Int
    ): Outcome<PopularMovies, BaseError> {
        return try {
            Outcome.Success(remoteDataSource.getMovies(language, page).toModel())
        } catch (t: Throwable) {
            Outcome.Error(networkErrorHandler.handleError(t))
        }
    }

    override suspend fun getGenres(language: String): Outcome<List<Genre>, BaseError> {
        return try {
            Outcome.Success(remoteDataSource.getGenres(language).toGenreList())
        } catch (t: Throwable) {
            Outcome.Error(networkErrorHandler.handleError(t))
        }
    }

    override suspend fun getMovie(
        language: String,
        movieId: Int
    ): Outcome<MovieDetail, BaseError> {
        return try {
            Outcome.Success(remoteDataSource.getMovie(language, movieId).toMovieDetail())
        } catch (t: Throwable) {
            Outcome.Error(networkErrorHandler.handleError(t))
        }
    }

    override suspend fun getMovieWithRelationships(
        language: String,
        movieId: Int
    ): Outcome<MovieGenresIds<MoviesResponse, GenreDto, Int>, BaseError> {
        return try {
            Outcome.Success(
                remoteDataSource.getMovieGenresAndIds(
                    language,
                    movieId
                )
            )
        } catch (t: Throwable) {
            Outcome.Error(networkErrorHandler.handleError(t))
        }
    }

    override suspend fun getMovieDetail(
        movieId: Int,
        language: String
    ): Outcome<MovieDetail, BaseError> {
        return try {
            Outcome.Success(remoteDataSource.getMovie(language, movieId).toMovieDetail())
        } catch (t: Throwable) {
            Outcome.Error(networkErrorHandler.handleError(t))
        }
    }

    override suspend fun getMovieVideos(
        language: String,
        id: Int
    ): Outcome<List<MovieVideo>, BaseError> {
        return try {
            Outcome.Success(
                remoteDataSource.getMovieVideos(language, id).map { dto -> dto.toMovieVideo() })
        } catch (t: Throwable) {
            Outcome.Error(networkErrorHandler.handleError(t))
        }
    }

}
package com.bajobozic.network.data.repository

import com.bajobozic.network.data.dto.toGenreList
import com.bajobozic.network.data.dto.toModel
import com.bajobozic.network.data.dto.toMovieDetail
import com.bajobozic.network.data.dto.toMovieVideo
import com.bajobozic.network.domain.model.PopularMovies
import com.bajobozic.shared_component.BaseError
import com.bajobozic.shared_component.Outcome


internal class NetworkRepositoryImpl(
    private val remoteDataSource: com.bajobozic.network.data.client.RemoteDataSource,
    private val networkErrorHandler: com.bajobozic.network.data.util.NetworkErrorHandler
) : com.bajobozic.network.domain.repository.NetworkRepository {
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

    override suspend fun getGenres(language: String): Outcome<List<com.bajobozic.network.domain.model.Genre>, BaseError> {
        return try {
            Outcome.Success(remoteDataSource.getGenres(language).toGenreList())
        } catch (t: Throwable) {
            Outcome.Error(networkErrorHandler.handleError(t))
        }
    }

    override suspend fun getMovie(
        language: String,
        movieId: Int
    ): Outcome<com.bajobozic.network.domain.model.MovieDetail, BaseError> {
        return try {
            Outcome.Success(remoteDataSource.getMovie(language, movieId).toMovieDetail())
        } catch (t: Throwable) {
            Outcome.Error(networkErrorHandler.handleError(t))
        }
    }

    override suspend fun getMovieWithRelationships(
        language: String,
        movieId: Int
    ): Outcome<com.bajobozic.network.data.dto.MovieGenresIds<com.bajobozic.network.data.dto.MoviesResponse, com.bajobozic.network.data.dto.GenreDto, Int>, BaseError> {
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
    ): Outcome<com.bajobozic.network.domain.model.MovieDetail, BaseError> {
        return try {
            Outcome.Success(remoteDataSource.getMovie(language, movieId).toMovieDetail())
        } catch (t: Throwable) {
            Outcome.Error(networkErrorHandler.handleError(t))
        }
    }

    override suspend fun getMovieVideos(
        language: String,
        id: Int
    ): Outcome<List<com.bajobozic.network.domain.model.MovieVideo>, BaseError> {
        return try {
            Outcome.Success(
                remoteDataSource.getMovieVideos(language, id).map { dto -> dto.toMovieVideo() })
        } catch (t: Throwable) {
            Outcome.Error(networkErrorHandler.handleError(t))
        }
    }

}
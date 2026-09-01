package com.bajobozic.storage.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import com.bajobozic.storage.data.source.LocalDataSource
import com.bajobozic.storage.domain.model.Genre
import com.bajobozic.storage.domain.model.GetMovieWithGenres
import com.bajobozic.storage.domain.model.GetTvShow
import com.bajobozic.storage.domain.model.Movie
import com.bajobozic.storage.domain.model.MovieDetail
import com.bajobozic.storage.domain.model.MovieRemoteKeysModel
import com.bajobozic.storage.domain.model.TvShowDetail
import com.bajobozic.storage.domain.model.TvShowRemoteKeysModel
import com.bajobozic.storage.domain.repository.StorageRepository
import kotlinx.coroutines.flow.Flow

internal class StorageRepositoryImpl @OptIn(ExperimentalPagingApi::class) constructor(
    private val localDataSource: LocalDataSource
) : StorageRepository {
    override fun getPagingSource(): PagingSource<Int, GetMovieWithGenres> =
        localDataSource.getPagingSource()

    override fun getTvShowPagingSource(): PagingSource<Int, GetTvShow> =
        localDataSource.getTvShowPagingSource()

    override suspend fun getAllGenres(): List<Genre> = localDataSource.getAllGenres()

    override suspend fun getMaxCurrentPage(): Int = localDataSource.getMaxCurrentPage() ?: 0

    override suspend fun getTvShowMaxCurrentPage(): Int =
        localDataSource.getTvShowMaxCurrentPage() ?: 0

    override fun getMovie(movieId: Int): Flow<Movie> = localDataSource.getMovie(movieId)

    override suspend fun insertAllMovies(
        list: List<MovieDetail>,
        genreList: List<Genre>,
        genreIdsPerMovie: List<List<Genre>>
    ) = localDataSource.insertAllMovies(list, genreList, genreIdsPerMovie)

    override suspend fun deleteThenInsertAllMovies(
        list: List<MovieDetail>,
        genreList: List<Genre>,
        genreIdsPerMovie: List<List<Genre>>
    ) = localDataSource.deleteThenInsertAllMovies(list, genreList, genreIdsPerMovie)

    override suspend fun insertAllTvShows(list: List<TvShowDetail>) =
        localDataSource.insertAllTvShows(list)

    override suspend fun deleteThenInsertAllTvShows(list: List<TvShowDetail>) =
        localDataSource.deleteThenInsertAllTvShows(list)

    override suspend fun deleteMovie(movieId: Int) = localDataSource.deleteMovie(movieId)

    override suspend fun batchTransaction(block: suspend () -> Unit) =
        localDataSource.batchTransaction(block)

    override suspend fun getMovieWithRemoteKeys(movieId: Int): MovieRemoteKeysModel {
        return localDataSource.getMovieWithRemoteKeys(movieId)
            ?: MovieRemoteKeysModel(movieId = movieId, prevKey = null, nextKey = null)
    }

    override suspend fun getTvShowWithRemoteKeys(tvShowId: Int): TvShowRemoteKeysModel {
        return localDataSource.getTvShowWithRemoteKeys(tvShowId)
            ?: TvShowRemoteKeysModel(tvShowId = tvShowId, prevKey = null, nextKey = null)
    }

    override suspend fun clearRemoteKeys() = localDataSource.clearRemoteKeys()

    override suspend fun clearTvShowRemoteKeys() = localDataSource.clearTvShowRemoteKeys()

    override suspend fun insertAllRemoteKeys(keys: List<MovieRemoteKeysModel>) =
        localDataSource.insertAllRemoteKeys(keys)

    override suspend fun insertAllTvShowRemoteKeys(keys: List<TvShowRemoteKeysModel>) =
        localDataSource.insertAllTvShowRemoteKeys(keys)
}
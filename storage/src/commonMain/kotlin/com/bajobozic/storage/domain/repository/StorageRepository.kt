package com.bajobozic.storage.domain.repository

import androidx.paging.PagingSource
import com.bajobozic.storage.domain.model.Genre
import com.bajobozic.storage.domain.model.GetMovieWithGenres
import com.bajobozic.storage.domain.model.GetTvShow
import com.bajobozic.storage.domain.model.Movie
import com.bajobozic.storage.domain.model.MovieDetail
import com.bajobozic.storage.domain.model.MovieRemoteKeysModel
import com.bajobozic.storage.domain.model.TvShowDetail
import com.bajobozic.storage.domain.model.TvShowRemoteKeysModel
import kotlinx.coroutines.flow.Flow

internal interface StorageRepository {
    fun getPagingSource(): PagingSource<Int, GetMovieWithGenres>
    fun getTvShowPagingSource(): PagingSource<Int, GetTvShow>
    suspend fun getAllGenres(): List<Genre>
    suspend fun getMaxCurrentPage(): Int
    suspend fun getTvShowMaxCurrentPage(): Int
    fun getMovie(movieId: Int): Flow<Movie>
    suspend fun insertAllMovies(
        list: List<MovieDetail>,
        genreList: List<Genre>,
        genreIdsPerMovie: List<List<Genre>>
    )
    suspend fun deleteThenInsertAllMovies(
        list: List<MovieDetail>,
        genreList: List<Genre>,
        genreIdsPerMovie: List<List<Genre>>
    )
    suspend fun insertAllTvShows(
        list: List<TvShowDetail>
    )

    suspend fun deleteThenInsertAllTvShows(
        list: List<TvShowDetail>
    )
    suspend fun deleteMovie(movieId: Int)
    suspend fun batchTransaction(block: suspend () -> Unit)
    suspend fun getMovieWithRemoteKeys(movieId: Int): MovieRemoteKeysModel
    suspend fun getTvShowWithRemoteKeys(tvShowId: Int): TvShowRemoteKeysModel
    suspend fun clearRemoteKeys(): Unit
    suspend fun clearTvShowRemoteKeys(): Unit
    suspend fun insertAllRemoteKeys(keys: List<MovieRemoteKeysModel>): Unit
    suspend fun insertAllTvShowRemoteKeys(keys: List<TvShowRemoteKeysModel>): Unit
}
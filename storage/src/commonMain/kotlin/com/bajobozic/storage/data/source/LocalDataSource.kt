package com.bajobozic.storage.data.source


import androidx.paging.PagingSource
import com.bajobozic.storage.data.entity.GenreEntity
import com.bajobozic.storage.data.entity.MovieEntity
import com.bajobozic.storage.data.entity.MovieRemoteKeys
import com.bajobozic.storage.data.entity.MovieWithGenres
import com.bajobozic.storage.data.entity.TvShowEntity
import com.bajobozic.storage.data.entity.TvShowRemoteKeys
import kotlinx.coroutines.flow.Flow

internal interface LocalDataSource {
    suspend fun insertAllMovies(
        list: List<MovieEntity>,
        genreList: List<GenreEntity>,
        genreIdsPerMovie: List<List<Int>>
    )

    suspend fun deleteThenInsertAllMovies(
        list: List<MovieEntity>,
        genreList: List<GenreEntity>,
        genreIdsPerMovie: List<List<Int>>
    )

    suspend fun insertAllTvShows(
        list: List<TvShowEntity>
    )

    suspend fun deleteThenInsertAllTvShows(
        list: List<TvShowEntity>
    )

    suspend fun deleteMovie(movieId: Int)
    suspend fun clearAll()
    suspend fun clearAllTvShows()

    suspend fun batchTransaction(block: suspend () -> Unit)

    suspend fun getMaxCurrentPage(): Int?
    suspend fun getTvShowMaxCurrentPage(): Int?

    fun getPagingSource(): PagingSource<Int, MovieWithGenres>
    fun getTvShowPagingSource(): PagingSource<Int, TvShowEntity>

    fun getMovie(movieId: Int): Flow<MovieWithGenres>

    suspend fun getAllGenres(): List<GenreEntity>

    suspend fun getMovieWithRemoteKeys(movieId: Int): MovieRemoteKeys?
    suspend fun clearRemoteKeys(): Unit
    suspend fun insertAllRemoteKeys(localKeys: List<MovieRemoteKeys>)

    suspend fun getTvShowWithRemoteKeys(tvShowId: Int): TvShowRemoteKeys?
    suspend fun clearTvShowRemoteKeys(): Unit
    suspend fun insertAllTvShowRemoteKeys(localKeys: List<TvShowRemoteKeys>)
}
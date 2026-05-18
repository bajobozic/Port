package com.bajobozic.storage.data.source

import androidx.paging.PagingSource
import androidx.room.Transaction
import com.bajobozic.storage.data.db.AppDatabase
import com.bajobozic.storage.data.entity.GenreEntity
import com.bajobozic.storage.data.entity.MovieEntity
import com.bajobozic.storage.data.entity.MovieRemoteKeys
import com.bajobozic.storage.data.entity.MovieWithGenres
import com.bajobozic.storage.data.entity.TvShowEntity
import com.bajobozic.storage.data.entity.TvShowRemoteKeys
import kotlinx.coroutines.flow.Flow

internal class LocalDataSourceImpl(private val appDatabase: AppDatabase) :
    LocalDataSource {
    override suspend fun insertAllMovies(
        list: List<MovieEntity>,
        genreList: List<GenreEntity>,
        genreIdsPerMovie: List<List<Int>>
    ) {
        appDatabase.getMovieDao().insertAll(list, genreList, genreIdsPerMovie)
    }

    override suspend fun deleteThenInsertAllMovies(
        list: List<MovieEntity>,
        genreList: List<GenreEntity>,
        genreIdsPerMovie: List<List<Int>>
    ) {
        appDatabase.getMovieDao().deleteThenInsertAll(list, genreList, genreIdsPerMovie)
    }

    override suspend fun insertAllTvShows(list: List<TvShowEntity>) {
        appDatabase.getTvShowDao().insertTvShows(list)
    }

    override suspend fun deleteThenInsertAllTvShows(list: List<TvShowEntity>) {
        appDatabase.getTvShowDao().clearAll()
        appDatabase.getTvShowDao().insertTvShows(list)
    }

    override suspend fun deleteMovie(movieId: Int) {
        appDatabase.getMovieDao().deleteMovie(movieId)
    }

    override suspend fun clearAll() {
        appDatabase.getMovieDao().clearAll()
    }

    override suspend fun clearAllTvShows() {
        appDatabase.getTvShowDao().clearAll()
    }

    @Transaction
    override suspend fun batchTransaction(block: suspend () -> Unit) {
        block()
    }

    override suspend fun getMaxCurrentPage(): Int? {
        return appDatabase.getMovieDao().getMaxCurrentPage()
    }

    override suspend fun getTvShowMaxCurrentPage(): Int? {
        return appDatabase.getTvShowDao().getMaxCurrentPage()
    }

    override fun getPagingSource(): PagingSource<Int, MovieWithGenres> {
        return appDatabase.getMovieDao().pagingSource()
    }

    override fun getTvShowPagingSource(): PagingSource<Int, TvShowEntity> {
        return appDatabase.getTvShowDao().pagingSource()
    }

    override fun getMovie(movieId: Int): Flow<MovieWithGenres> {
        return appDatabase.getMovieDao().getMovieFlow(movieId)
    }

    override suspend fun getAllGenres(): List<GenreEntity> {
        return appDatabase.getMovieDao().getAllGenres()
    }

    override suspend fun getMovieWithRemoteKeys(movieId: Int): MovieRemoteKeys? {
        return appDatabase.getMovieRemoteKeysDao().remoteKeysByMovieId(movieId)
    }

    override suspend fun clearRemoteKeys() {
        appDatabase.getMovieRemoteKeysDao().clearRemoteKeys()
    }

    override suspend fun insertAllRemoteKeys(localKeys: List<MovieRemoteKeys>) {
        appDatabase.getMovieRemoteKeysDao().insertAll(localKeys)
    }

    override suspend fun getTvShowWithRemoteKeys(tvShowId: Int): TvShowRemoteKeys? {
        return appDatabase.getTvShowRemoteKeysDao().remoteKeysByTvShowId(tvShowId)
    }

    override suspend fun clearTvShowRemoteKeys() {
        appDatabase.getTvShowRemoteKeysDao().clearRemoteKeys()
    }

    override suspend fun insertAllTvShowRemoteKeys(localKeys: List<TvShowRemoteKeys>) {
        appDatabase.getTvShowRemoteKeysDao().insertAll(localKeys)
    }
}
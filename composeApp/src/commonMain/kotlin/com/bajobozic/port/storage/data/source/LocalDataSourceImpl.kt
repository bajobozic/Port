package com.bajobozic.port.storage.data.source

import androidx.paging.PagingSource
import androidx.room.Transaction
import com.bajobozic.port.storage.data.db.AppDatabase
import com.bajobozic.port.storage.data.entity.GenreEntity
import com.bajobozic.port.storage.data.entity.MovieEntity
import com.bajobozic.port.storage.data.entity.MovieRemoteKeys
import com.bajobozic.port.storage.data.entity.MovieWithGenres
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

    override suspend fun deleteMovie(movieId: Int) {
        appDatabase.getMovieDao().deleteMovie(movieId)
    }

    override suspend fun clearAll() {
        appDatabase.getMovieDao().clearAll()
    }

    @Transaction
    override suspend fun batchTransaction(block: suspend () -> Unit) {
        block()
    }

    override suspend fun getMaxCurrentPage(): Int? {
        return appDatabase.getMovieDao().getMaxCurrentPage()
    }

    override fun getPagingSource(): PagingSource<Int, MovieWithGenres> {
        return appDatabase.getMovieDao().pagingSource()
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
}
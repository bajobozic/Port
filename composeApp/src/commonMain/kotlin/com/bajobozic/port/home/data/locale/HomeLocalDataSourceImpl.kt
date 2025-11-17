package com.bajobozic.port.home.data.locale

import androidx.paging.PagingSource
import com.bajobozic.port.home.data.locale.db.AppDatabase
import com.bajobozic.port.home.data.locale.entity.GenreEntity
import com.bajobozic.port.home.data.locale.entity.MovieEntity
import com.bajobozic.port.home.data.locale.entity.MovieWithGenres
import kotlinx.coroutines.flow.Flow

internal class HomeLocalDataSourceImpl(private val appDatabase: AppDatabase) :
    HomeLocalDataSource {
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

    override suspend fun batchTransaction(block: suspend () -> Unit) {
//        appDatabase.withTransaction { block() }
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
}
package com.bajobozic.storage.data.source


import androidx.paging.PagingSource
import com.bajobozic.storage.data.entity.GenreEntity
import com.bajobozic.storage.data.entity.MovieEntity
import com.bajobozic.storage.data.entity.MovieRemoteKeys
import com.bajobozic.storage.data.entity.MovieWithGenres
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

    suspend fun deleteMovie(movieId: Int)
    suspend fun clearAll()

    suspend fun batchTransaction(block: suspend () -> Unit)

    suspend fun getMaxCurrentPage(): Int?

    fun getPagingSource(): PagingSource<Int, MovieWithGenres>

    fun getMovie(movieId: Int): Flow<MovieWithGenres>

    suspend fun getAllGenres(): List<GenreEntity>

    suspend fun getMovieWithRemoteKeys(movieId: Int): MovieRemoteKeys?
    suspend fun clearRemoteKeys(): Unit
    suspend fun insertAllRemoteKeys(localKeys: List<MovieRemoteKeys>)
}
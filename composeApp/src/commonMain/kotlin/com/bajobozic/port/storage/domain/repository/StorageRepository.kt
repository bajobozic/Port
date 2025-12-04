package com.bajobozic.port.storage.domain.repository

import androidx.paging.PagingSource
import com.bajobozic.port.network.domain.model.MovieDetail
import com.bajobozic.port.storage.domain.model.Genre
import com.bajobozic.port.storage.domain.model.GetMovieWithGenres
import com.bajobozic.port.storage.domain.model.Movie
import com.bajobozic.port.storage.domain.model.MovieRemoteKeysModel
import kotlinx.coroutines.flow.Flow

interface StorageRepository {
    fun getPagingSource(): PagingSource<Int, GetMovieWithGenres>
    suspend fun getAllGenres(): List<Genre>
    suspend fun getMaxCurrentPage(): Int
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
    suspend fun batchTransaction(block: suspend () -> Unit)
    suspend fun getMovieWithRemoteKeys(movieId: Int): MovieRemoteKeysModel
    suspend fun clearRemoteKeys(): Unit
    suspend fun insertAllRemoteKeys(keys: List<MovieRemoteKeysModel>): Unit
}
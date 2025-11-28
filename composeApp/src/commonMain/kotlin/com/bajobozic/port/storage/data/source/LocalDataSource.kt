package com.bajobozic.port.storage.data.source


import androidx.paging.PagingSource
import com.bajobozic.port.storage.data.entity.GenreEntity
import com.bajobozic.port.storage.data.entity.MovieEntity
import com.bajobozic.port.storage.data.entity.MovieWithGenres
import com.bajobozic.port.storage.domain.model.GetMovieWithGenres
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

    fun getPagingSource(): PagingSource<Int, GetMovieWithGenres>

    fun getMovie(movieId: Int): Flow<MovieWithGenres>

    suspend fun getAllGenres(): List<GenreEntity>
}
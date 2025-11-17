package com.bajobozic.port.home.data.locale


import androidx.paging.PagingSource
import com.bajobozic.port.home.data.locale.entity.GenreEntity
import core.data.local.entity.MovieEntity
import home.data.local.db.MovieWithGenres
import kotlinx.coroutines.flow.Flow

interface HomeLocalDataSource {
    suspend fun insertAllMovies(
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
}
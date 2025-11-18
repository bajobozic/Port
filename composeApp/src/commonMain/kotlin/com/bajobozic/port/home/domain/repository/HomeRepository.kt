package com.bajobozic.port.home.domain.repository

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.bajobozic.port.home.domain.model.GetMoviesWithGenres
import com.bajobozic.port.home.domain.model.Movie
import com.bajobozic.port.home.domain.model.MovieDetail
import com.bajobozic.port.home.domain.model.MovieVideo
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    suspend fun deleteMovie(movieId: Int)

    fun <T : GetMoviesWithGenres> getPagingSource(): PagingSource<Int, GetMoviesWithGenres>

    fun getPagingData(language: String): Flow<PagingData<Movie>>

    suspend fun getMovieDetail(movieId: Int, language: String): Flow<MovieDetail>

    suspend fun getMovieVideo(id: Int, language: String): List<MovieVideo>
}
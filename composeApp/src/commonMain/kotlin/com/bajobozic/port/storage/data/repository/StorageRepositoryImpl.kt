package com.bajobozic.port.storage.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.RemoteMediator
import androidx.paging.map
import com.bajobozic.port.network.domain.model.MovieDetail
import com.bajobozic.port.storage.data.entity.MovieEntity
import com.bajobozic.port.storage.data.entity.MovieWithGenres
import com.bajobozic.port.storage.data.entity.toModel
import com.bajobozic.port.storage.data.source.LocalDataSource
import com.bajobozic.port.storage.domain.model.Genre
import com.bajobozic.port.storage.domain.model.GetMoviesWithGenres
import com.bajobozic.port.storage.domain.model.Movie
import com.bajobozic.port.storage.domain.repository.StorageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class StorageRepositoryImpl @OptIn(ExperimentalPagingApi::class) constructor(
    private val localDataSource: LocalDataSource,
    private val remoteMediatorFactory: RemoteMediator<Int, MovieWithGenres>
) : StorageRepository {
    @Suppress("UNCHECKED_CAST")
    override fun <T : GetMoviesWithGenres> getPagingSource(): PagingSource<Int, GetMoviesWithGenres> {
        return localDataSource.getPagingSource() as PagingSource<Int, GetMoviesWithGenres>
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagingData(language: String): Flow<PagingData<Movie>> {

        val pager = Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 5, initialLoadSize = 20),
            initialKey = 0,
            pagingSourceFactory = {
                localDataSource.getPagingSource()
            },
            remoteMediator = remoteMediatorFactory
        )
        return pager.flow.map { pagingData -> pagingData.map { movieWithGenres -> movieWithGenres.toModel() } }
    }

    override suspend fun getAllGenres(): List<Genre> {
        return localDataSource.getAllGenres().map { genreEntity -> genreEntity.toModel() }
    }

    override suspend fun getMaxCurrentPage(): Int {
        return localDataSource.getMaxCurrentPage() ?: 0
    }

    override fun getMovie(movieId: Int): Flow<Movie> {
        return localDataSource.getMovie(movieId)
            .map { movieWithGenres -> movieWithGenres.toModel() }
    }

    override suspend fun insertAllMovies(
        list: List<MovieDetail>,
        genreList: List<Genre>,
        genreIdsPerMovie: List<List<Genre>>
    ) {
        localDataSource.insertAllMovies(list = list.map { movieDetail -> movieDetail. })
    }

    override suspend fun deleteThenInsertAllMovies(
        list: List<MovieDetail>,
        genreList: List<Genre>,
        genreIdsPerMovie: List<List<Genre>>
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun batchTransaction(block: suspend () -> Unit) {
        localDataSource.batchTransaction { block }
    }

    private fun movieDetailToEntity(movieDetail: MovieDetail): MovieEntity {
        return MovieEntity(
            id = movieDetail.
        )
    }

}
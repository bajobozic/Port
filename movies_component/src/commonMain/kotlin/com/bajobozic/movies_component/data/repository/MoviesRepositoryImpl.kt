package com.bajobozic.movies_component.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.RemoteMediator
import androidx.paging.map
import com.bajobozic.movies_component.domain.repository.MoviesRepository
import com.bajobozic.storage.domain.model.GetMovieWithGenres
import com.bajobozic.storage.domain.model.Movie
import com.bajobozic.storage.domain.usecase.GetPagingSourceUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalPagingApi::class)
internal class MoviesRepositoryImpl(
    remoteMediatorFactory: RemoteMediator<Int, GetMovieWithGenres>,
    private val getPagingSourceUseCase: GetPagingSourceUseCase
) : MoviesRepository {
    private val pager = Pager(
        config = PagingConfig(pageSize = 20, prefetchDistance = 5, initialLoadSize = 20),
        initialKey = 0,
        pagingSourceFactory = {
            getPagingSourceUseCase()
        },
        remoteMediator = remoteMediatorFactory
    )

    override fun getPagingData(language: String): Flow<PagingData<Movie>> {
        return pager.flow.map { pagingData -> pagingData.map { it.toModel() } }
    }
}

package com.bajobozic.port.home_component.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.RemoteMediator
import androidx.paging.map
import com.bajobozic.port.home_component.domain.repository.HomeRepository
import com.bajobozic.port.storage.domain.model.GetMovieWithGenres
import com.bajobozic.port.storage.domain.model.Movie
import com.bajobozic.port.storage.domain.usecase.GetPagingSourceUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalPagingApi::class)
internal class HomeRepositoryImp(
    private val remoteMediatorFactory: RemoteMediator<Int, GetMovieWithGenres>,
    private val getPagingSourceUseCase: GetPagingSourceUseCase
) : HomeRepository {
    val pager = Pager(
        config = PagingConfig(pageSize = 20, prefetchDistance = 5, initialLoadSize = 20),
        initialKey = 0,
        pagingSourceFactory = {
            getPagingSourceUseCase()
        },
        remoteMediator = remoteMediatorFactory
    )

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagingData(language: String): Flow<PagingData<Movie>> {
        return pager.flow.map { pagingData -> pagingData.map { it.toModel() } }
    }
}
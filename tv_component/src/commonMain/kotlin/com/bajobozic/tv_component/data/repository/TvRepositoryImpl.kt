package com.bajobozic.tv_component.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.RemoteMediator
import androidx.paging.map
import com.bajobozic.storage.domain.model.GetTvShow
import com.bajobozic.storage.domain.model.TvShow
import com.bajobozic.storage.domain.usecase.GetTvShowPagingSourceUseCase
import com.bajobozic.tv_component.domain.repository.TvRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalPagingApi::class)
internal class TvRepositoryImpl(
    remoteMediatorFactory: RemoteMediator<Int, GetTvShow>,
    private val getTvShowPagingSourceUseCase: GetTvShowPagingSourceUseCase
) : TvRepository {
    private val pager = Pager(
        config = PagingConfig(pageSize = 20, prefetchDistance = 5, initialLoadSize = 20),
        initialKey = 0,
        pagingSourceFactory = {
            getTvShowPagingSourceUseCase()
        },
        remoteMediator = remoteMediatorFactory
    )

    override fun getPagingData(language: String): Flow<PagingData<TvShow>> {
        return pager.flow.map { pagingData -> pagingData.map { it.toModel() } }
    }
}

package com.bajobozic.tv_component.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.bajobozic.core_component.BaseError
import com.bajobozic.core_component.Outcome
import com.bajobozic.network.domain.model.initRemoteKeys
import com.bajobozic.network.domain.usecase.GetTvShowsUseCase
import com.bajobozic.storage.domain.model.GetTvShow
import com.bajobozic.storage.domain.model.TvShowDetail
import com.bajobozic.storage.domain.model.TvShowRemoteKeysModel
import com.bajobozic.storage.domain.usecase.BatchTransactionUseCase
import com.bajobozic.storage.domain.usecase.ClearTvShowRemoteKeysUseCase
import com.bajobozic.storage.domain.usecase.DeleteThenInsertAllTvShowsUseCase
import com.bajobozic.storage.domain.usecase.GetRemoteKeysByTvShowIdUseCase
import com.bajobozic.storage.domain.usecase.InsertAllTvShowsUseCase
import com.bajobozic.storage.domain.usecase.TvShowRemoteKeysInsertAllUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.coroutineScope

@OptIn(ExperimentalPagingApi::class)
internal class TvShowRemoteMediator(
    private val getTvShowsUseCase: GetTvShowsUseCase,
    private val deleteThenInsertAllTvShowsUseCase: DeleteThenInsertAllTvShowsUseCase,
    private val insertAllTvShowsUseCase: InsertAllTvShowsUseCase,
    private val batchTransactionUseCase: BatchTransactionUseCase,
    private val getRemoteKeysByTvShowIdUseCase: GetRemoteKeysByTvShowIdUseCase,
    private val clearTvShowRemoteKeysUseCase: ClearTvShowRemoteKeysUseCase,
    private val tvShowRemoteKeysInsertAllUseCase: TvShowRemoteKeysInsertAllUseCase
) : RemoteMediator<Int, GetTvShow>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, GetTvShow>
    ): MediatorResult {
        return try {
            val page = getPage(loadType, state)
                ?: return MediatorResult.Success(endOfPaginationReached = false)

            val tvShows = fetchTvShowData(page)

            val endOfPaginationReached = tvShows.isEmpty()
            val prevKey = if (page > 1) page - 1 else null
            val nextKey = if (endOfPaginationReached) null else page + 1

            val remoteKeys = tvShows.map { tvShow ->
                TvShowRemoteKeysModel(tvShow.id, prevKey, nextKey)
            }

            batchTransactionUseCase {
                if (loadType == LoadType.REFRESH) {
                    clearTvShowRemoteKeysUseCase()
                    deleteThenInsertAllTvShowsUseCase(
                        tvShows.map { tvShowDetail ->
                            TvShowDetail(
                                id = tvShowDetail.id,
                                backdropPath = tvShowDetail.backdropPath,
                                originalLanguage = tvShowDetail.originalLanguage,
                                overview = tvShowDetail.overview,
                                popularity = tvShowDetail.popularity,
                                posterPath = tvShowDetail.posterPath,
                                firstAirDate = tvShowDetail.firstAirDate,
                                name = tvShowDetail.name,
                                voteAverage = tvShowDetail.voteAverage,
                                voteCount = tvShowDetail.voteCount,
                                originalName = tvShowDetail.originalName
                            ).apply { currentPage = tvShowDetail.currentPage }
                        })
                } else {
                    insertAllTvShowsUseCase(tvShows.map { tvShowDetail ->
                        TvShowDetail(
                            id = tvShowDetail.id,
                            backdropPath = tvShowDetail.backdropPath,
                            originalLanguage = tvShowDetail.originalLanguage,
                            overview = tvShowDetail.overview,
                            popularity = tvShowDetail.popularity,
                            posterPath = tvShowDetail.posterPath,
                            firstAirDate = tvShowDetail.firstAirDate,
                            name = tvShowDetail.name,
                            voteAverage = tvShowDetail.voteAverage,
                            voteCount = tvShowDetail.voteCount,
                            originalName = tvShowDetail.originalName
                        ).apply { currentPage = tvShowDetail.currentPage }
                    })
                }
                tvShowRemoteKeysInsertAllUseCase(remoteKeys)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            if (e is CancellationException)
                throw e
            else
                MediatorResult.Error(e)
        }
    }

    private suspend fun fetchTvShowData(page: Int) = coroutineScope {
        val tvShowsOutcome = getTvShowsUseCase("en-US", page)

        when (tvShowsOutcome) {
            is Outcome.Success -> {
                tvShowsOutcome.data.initRemoteKeys().tvShows
            }

            is Outcome.Error -> {
                throw RuntimeException(getError(tvShowsOutcome.error))
            }
        }
    }

    private suspend fun getPage(
        loadType: LoadType,
        state: PagingState<Int, GetTvShow>
    ): Int? {
        return when (loadType) {
            LoadType.REFRESH -> {
                val anchorItem = state.anchorPosition?.let { state.closestItemToPosition(it) }
                val remoteKeys =
                    anchorItem?.toModel()?.id?.let { getRemoteKeysByTvShowIdUseCase(it) }
                remoteKeys?.prevKey?.plus(1) ?: remoteKeys?.nextKey?.minus(1) ?: 1
            }

            LoadType.PREPEND -> {
                val firstItem = state.firstItemOrNull() ?: return null
                val keys = getRemoteKeysByTvShowIdUseCase(firstItem.toModel().id)
                keys.prevKey
            }

            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull() ?: return null
                val keys = getRemoteKeysByTvShowIdUseCase(lastItem.toModel().id)
                keys.nextKey
            }
        }
    }

    private fun getError(error: BaseError): String {
        return when (error) {
            is BaseError.ApiError.HttpServerError -> error.message
            BaseError.ApiError.NoInternet -> "Check internet connection"
            BaseError.ApiError.ToManyRequest -> "Too many requests"
            is BaseError.ApiError.UnknownError -> error.message
            else -> "Unknown Error"
        }
    }
}

@file:OptIn(ExperimentalPagingApi::class)

package com.bajobozic.port.home.data.remote.client

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.bajobozic.port.home.data.locale.HomeLocalDataSource
import com.bajobozic.port.home.data.remote.dto.initRemoteKeys
import com.bajobozic.port.home.data.remote.dto.toEntity
import home.data.local.db.MovieWithGenres

class MovieRemoteMediator(
    private val homeRemoteDataSource: HomeRemoteDataSource,
    private val homeLocalDataSource: HomeLocalDataSource
) : RemoteMediator<Int, MovieWithGenres>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieWithGenres>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> state.lastItemOrNull()?.movie?.currentPage ?: 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val keyFromBase =
                        homeLocalDataSource.getMaxCurrentPage() ?: 0
                    keyFromBase + 1
                }
            }

            val moviesResponse =
                homeRemoteDataSource.getMovies("en-us", loadKey).initRemoteKeys().movies
            val genreResponse = homeRemoteDataSource.getGenres("en-us").genres
            val genreIdsPerMovie = moviesResponse.map { it.genreIds }
            homeLocalDataSource.batchTransaction {
                if (loadType == LoadType.REFRESH)
                    homeLocalDataSource.clearAll()

                homeLocalDataSource.insertAllMovies(
                    moviesResponse.map { it.toEntity() },
                    genreResponse.map { it.toEntity() },
                    genreIdsPerMovie
                )
            }

            MediatorResult.Success(
                endOfPaginationReached = moviesResponse.isEmpty()
            )
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }
}
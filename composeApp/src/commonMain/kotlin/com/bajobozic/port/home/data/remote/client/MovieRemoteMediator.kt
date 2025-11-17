@file:OptIn(ExperimentalPagingApi::class)

package com.bajobozic.port.home.data.remote.client

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.bajobozic.port.home.data.locale.HomeLocalDataSource
import com.bajobozic.port.home.data.locale.entity.GenreEntity
import com.bajobozic.port.home.data.locale.entity.MovieWithGenres
import com.bajobozic.port.home.data.remote.dto.initRemoteKeys
import com.bajobozic.port.home.data.remote.dto.toEntity

class MovieRemoteMediator(
    private val homeRemoteDataSource: HomeRemoteDataSource,
    private val homeLocalDataSource: HomeLocalDataSource
) : RemoteMediator<Int, MovieWithGenres>() {
    var firstTimeUpdateGenres = true
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
                homeRemoteDataSource.getMovies("en-US", loadKey).initRemoteKeys().movies
            val genreResponse = getAllGenres()
            val genreIdsPerMovie = moviesResponse.map { it.genreIds }
            homeLocalDataSource.batchTransaction {
                if (loadType == LoadType.REFRESH) {
                    homeLocalDataSource.clearAll()
                }

                homeLocalDataSource.insertAllMovies(
                    list = moviesResponse.map { it.toEntity() },
                    genreList = genreResponse,
                    genreIdsPerMovie = genreIdsPerMovie
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

    private suspend fun getAllGenres(): List<GenreEntity> {
        val genresList = homeLocalDataSource.getAllGenres()
        if (genresList.isNotEmpty() && !firstTimeUpdateGenres) return genresList

        firstTimeUpdateGenres = false
        return homeRemoteDataSource.getGenres("en-US").genres.map { it.toEntity() }
    }
}
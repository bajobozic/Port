package com.bajobozic.port.home_component.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.bajobozic.port.network.domain.model.initRemoteKeys
import com.bajobozic.port.network.domain.usecase.GetGenresUseCase
import com.bajobozic.port.network.domain.usecase.GetMoviesUseCase
import com.bajobozic.port.shared_component.domain.BaseError
import com.bajobozic.port.shared_component.domain.Outcome
import com.bajobozic.port.storage.domain.model.Genre
import com.bajobozic.port.storage.domain.model.GetMovieWithGenres
import com.bajobozic.port.storage.domain.model.MovieRemoteKeysModel
import com.bajobozic.port.storage.domain.usecase.BatchTransactionUseCase
import com.bajobozic.port.storage.domain.usecase.ClearRemoteKeysUseCase
import com.bajobozic.port.storage.domain.usecase.DeleteThenInsertAllMoviesUseCase
import com.bajobozic.port.storage.domain.usecase.GetAllGenresUseCase
import com.bajobozic.port.storage.domain.usecase.GetRemoteKeysByMovieIdUseCase
import com.bajobozic.port.storage.domain.usecase.InsertAllMoviesUseCase
import com.bajobozic.port.storage.domain.usecase.RemoteKeysInsertAllUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

@OptIn(ExperimentalPagingApi::class)
internal class MovieRemoteMediator(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val deleteThenInsertAllMoviesUseCase: DeleteThenInsertAllMoviesUseCase,
    private val insertAllMoviesUseCase: InsertAllMoviesUseCase,
    private val getAllGenresUseCase: GetAllGenresUseCase,
    private val batchTransactionUseCase: BatchTransactionUseCase,
    private val getGenresUseCase: GetGenresUseCase,
    private val getRemoteKeysByMovieIdUseCase: GetRemoteKeysByMovieIdUseCase,
    private val clearRemoteKeysUseCase: ClearRemoteKeysUseCase,
    private val remoteKeysInsertAllUseCase: RemoteKeysInsertAllUseCase
) : RemoteMediator<Int, GetMovieWithGenres>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, GetMovieWithGenres>
    ): MediatorResult {
        return try {
            // 1. Calculate Page
            val page = getPage(loadType, state)
                ?: return MediatorResult.Success(endOfPaginationReached = false)

            // 2. Fetch Data (Parallel)
            // returns a Pair(Movies, Genres)
            val (movies, genres) = fetchMovieData(page)

            // 3. Prepare Keys
            val endOfPaginationReached = movies.isEmpty()
            val prevKey = if (page > 1) page - 1 else null
            val nextKey = if (endOfPaginationReached) null else page + 1

            val remoteKeys = movies.map { movie ->
                MovieRemoteKeysModel(movie.id, prevKey, nextKey)
            }

            // 4. Transaction
            batchTransactionUseCase {
                if (loadType == LoadType.REFRESH) {
                    clearRemoteKeysUseCase()
                    deleteThenInsertAllMoviesUseCase(movies, genres, movies.map { it.genreIds })
                } else {
                    insertAllMoviesUseCase(movies, genres, movies.map { it.genreIds })
                }
                remoteKeysInsertAllUseCase(remoteKeys)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    /**
     * Helper to fetch data concurrently.
     * Uses 'when' to unwrap your custom Outcome interface.
     */
    private suspend fun fetchMovieData(page: Int) = coroutineScope {
        val moviesDeferred = async { getMoviesUseCase("en-US", page) }
        val genresDeferred = async { getAllGenres() }

        val moviesOutcome = moviesDeferred.await()
        val genres = genresDeferred.await()

        // Unwrap the Outcome using 'when'
        val movies = when (moviesOutcome) {
            is Outcome.Success -> {
                // Apply your mapper logic here on the data
                moviesOutcome.data.initRemoteKeys().movies
            }

            is Outcome.Error -> {
                // Throw to catch block in load()
                throw RuntimeException(getError(moviesOutcome.error))
            }
        }

        movies to genres // Return Pair
    }

    private suspend fun getAllGenres(): List<Genre> {
        val cached = getAllGenresUseCase()
        if (cached.isNotEmpty()) return cached

        // Unwrap the Outcome using 'when'
        return when (val result = getGenresUseCase("en-US")) {
            is Outcome.Success -> {
                result.data.map { Genre(it.id, it.name) }
            }

            is Outcome.Error -> {
                emptyList() // Fallback to empty list on error
            }
        }
    }

    private suspend fun getPage(
        loadType: LoadType,
        state: PagingState<Int, GetMovieWithGenres>
    ): Int? {
        return when (loadType) {
            LoadType.REFRESH -> {
                val anchorItem = state.anchorPosition?.let { state.closestItemToPosition(it) }
                val remoteKeys =
                    anchorItem?.toModel()?.id?.let { getRemoteKeysByMovieIdUseCase(it) }
                remoteKeys?.prevKey?.plus(1) ?: remoteKeys?.nextKey?.minus(1) ?: 1
            }

            LoadType.PREPEND -> {
                val firstItem = state.firstItemOrNull() ?: return null
                val keys = getRemoteKeysByMovieIdUseCase(firstItem.toModel().id)
                keys.prevKey
            }

            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull() ?: return null
                val keys = getRemoteKeysByMovieIdUseCase(lastItem.toModel().id)
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
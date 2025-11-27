package com.bajobozic.port.home_component.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.bajobozic.port.network.data.dto.toModel
import com.bajobozic.port.network.domain.model.MovieDetail
import com.bajobozic.port.network.domain.model.initRemoteKeys
import com.bajobozic.port.network.domain.usecase.GetGenresUseCase
import com.bajobozic.port.network.domain.usecase.GetMoviesUseCase
import com.bajobozic.port.shared_component.domain.onError
import com.bajobozic.port.shared_component.domain.onSuccess
import com.bajobozic.port.storage.domain.model.Genre
import com.bajobozic.port.storage.domain.model.Movie
import com.bajobozic.port.storage.domain.usecase.BatchTransactionUseCase
import com.bajobozic.port.storage.domain.usecase.DeleteThenInsertAllMoviesUseCase
import com.bajobozic.port.storage.domain.usecase.GetAllGenresUseCase
import com.bajobozic.port.storage.domain.usecase.GetMaxCurrentPageUseCase
import com.bajobozic.port.storage.domain.usecase.InsertAllMoviesUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

@OptIn(ExperimentalPagingApi::class)
internal class MovieRemoteMediator(
    private val getMaxCurrentPageUseCase: GetMaxCurrentPageUseCase,
    private val getMoviesUseCase: GetMoviesUseCase,
    private val deleteThenInsertAllMoviesUseCase: DeleteThenInsertAllMoviesUseCase,
    private val insertAllMoviesUseCase: InsertAllMoviesUseCase,
    private val getAllGenresUseCase: GetAllGenresUseCase,
    private val batchTransactionUseCase: BatchTransactionUseCase,
    private val getGenresUseCase: GetGenresUseCase,
) : RemoteMediator<Int, Movie>() {
    var firstTimeUpdateGenres = true
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Movie>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> state.lastItemOrNull()?.currentPage ?: 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> getMaxCurrentPageUseCase() + 1
            }
            var moviesResponse = emptyList<MovieDetail>()
            var genreResponse = emptyList<Genre>()
            coroutineScope {
                val mrd = async { getMoviesUseCase("en-US", loadKey) }
                val grd = async { getAllGenres() }
                val mr = mrd.await()
                val gr = grd.await()
                mr.onSuccess { movies ->
                    moviesResponse = movies.initRemoteKeys().movies
                }.onError { _ -> emptyList<MovieDetail>() }
                genreResponse = gr
            }
            val genreIdsPerMovie = moviesResponse.map { it.genreIds }
            batchTransactionUseCase {
                if (loadType == LoadType.REFRESH) {
                    deleteThenInsertAllMoviesUseCase(
                        moviesResponse,
                        genreResponse,
                        genreIdsPerMovie
                    )
                } else {
                    insertAllMoviesUseCase(
                        moviesResponse,
                        genreResponse,
                        genreIdsPerMovie
                    )
                }
            }

            MediatorResult.Success(
                endOfPaginationReached = moviesResponse.isEmpty()
            )
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    override suspend fun initialize(): InitializeAction {
        // Decide whether to trigger initial refresh or not.In this case, we want to refresh
        // data on first launch, so we return LAUNCH_INITIAL_REFRESH the first time.
        return if (firstTimeUpdateGenres) InitializeAction.LAUNCH_INITIAL_REFRESH else InitializeAction.SKIP_INITIAL_REFRESH
    }

    private suspend fun getAllGenres(): List<Genre> {
        val genresList = getAllGenresUseCase()
        if (genresList.isNotEmpty() && !firstTimeUpdateGenres) return genresList

        firstTimeUpdateGenres = false
        return getGenresUseCase("en-US").genres.map { it.toModel() }
    }
}
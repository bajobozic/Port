package com.bajobozic.storage.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bajobozic.storage.domain.model.Genre
import com.bajobozic.storage.domain.model.GetMovieWithGenres
import com.bajobozic.storage.domain.model.GetTvShow
import com.bajobozic.storage.domain.model.Movie
import com.bajobozic.storage.domain.model.MovieDetail
import com.bajobozic.storage.domain.model.MovieRemoteKeysModel
import com.bajobozic.storage.domain.model.TvShow
import com.bajobozic.storage.domain.model.TvShowDetail
import com.bajobozic.storage.domain.model.TvShowRemoteKeysModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

internal class WasmLocalDataSourceImpl : LocalDataSource {
    private val movies = mutableListOf<MovieDetail>()
    private val tvShows = mutableListOf<TvShowDetail>()
    private val genres = mutableListOf<Genre>()
    private val movieRemoteKeys = mutableMapOf<Int, MovieRemoteKeysModel>()
    private val tvShowRemoteKeys = mutableMapOf<Int, TvShowRemoteKeysModel>()

    private val activeMoviePagingSources = mutableListOf<PagingSource<*, *>>()
    private val activeTvShowPagingSources = mutableListOf<PagingSource<*, *>>()
    private val movieFlows = mutableMapOf<Int, MutableStateFlow<Movie>>()

    private fun invalidateMoviePagingSources() {
        val sources = activeMoviePagingSources.toList()
        activeMoviePagingSources.clear()
        sources.forEach { it.invalidate() }
    }

    private fun invalidateTvShowPagingSources() {
        val sources = activeTvShowPagingSources.toList()
        activeTvShowPagingSources.clear()
        sources.forEach { it.invalidate() }
    }

    override suspend fun insertAllMovies(
        list: List<MovieDetail>,
        genreList: List<Genre>,
        genreIdsPerMovie: List<List<Genre>>
    ) {
        genres.addAll(genreList.distinctBy { it.id })
        val existingIds = movies.map { it.id }.toSet()
        val newMovies = list.filter { it.id !in existingIds }
        movies.addAll(newMovies)
        movies.forEach { m ->
            movieFlows[m.id]?.value = InMemoryMovieWithGenres(m).toModel()
        }
        invalidateMoviePagingSources()
    }

    override suspend fun deleteThenInsertAllMovies(
        list: List<MovieDetail>,
        genreList: List<Genre>,
        genreIdsPerMovie: List<List<Genre>>
    ) {
        genres.clear()
        genres.addAll(genreList.distinctBy { it.id })
        movies.clear()
        movies.addAll(list)
        movies.forEach { m ->
            movieFlows[m.id]?.value = InMemoryMovieWithGenres(m).toModel()
        }
        invalidateMoviePagingSources()
    }

    override suspend fun insertAllTvShows(list: List<TvShowDetail>) {
        val existingIds = tvShows.map { it.id }.toSet()
        val newTvShows = list.filter { it.id !in existingIds }
        tvShows.addAll(newTvShows)
        invalidateTvShowPagingSources()
    }

    override suspend fun deleteThenInsertAllTvShows(list: List<TvShowDetail>) {
        tvShows.clear()
        tvShows.addAll(list)
        invalidateTvShowPagingSources()
    }

    override suspend fun deleteMovie(movieId: Int) {
        movies.removeAll { it.id == movieId }
        invalidateMoviePagingSources()
    }

    override suspend fun clearAll() {
        movies.clear()
        movieRemoteKeys.clear()
        invalidateMoviePagingSources()
    }

    override suspend fun clearAllTvShows() {
        tvShows.clear()
        tvShowRemoteKeys.clear()
        invalidateTvShowPagingSources()
    }

    override suspend fun batchTransaction(block: suspend () -> Unit) {
        block()
    }

    override suspend fun getMaxCurrentPage(): Int? = movies.maxOfOrNull { it.currentPage }

    override suspend fun getTvShowMaxCurrentPage(): Int? = tvShows.maxOfOrNull { it.currentPage }

    override fun getPagingSource(): PagingSource<Int, GetMovieWithGenres> {
        val source = InMemoryPagingSource<GetMovieWithGenres> {
            movies.map { InMemoryMovieWithGenres(it) }
        }
        activeMoviePagingSources.add(source)
        source.registerInvalidatedCallback {
            activeMoviePagingSources.remove(source)
        }
        return source
    }

    override fun getTvShowPagingSource(): PagingSource<Int, GetTvShow> {
        val source = InMemoryPagingSource<GetTvShow> {
            tvShows.map { InMemoryTvShow(it) }
        }
        activeTvShowPagingSources.add(source)
        source.registerInvalidatedCallback {
            activeTvShowPagingSources.remove(source)
        }
        return source
    }

    override fun getMovie(movieId: Int): Flow<Movie> {
        val movie = movies.find { it.id == movieId }
        val initial = movie?.let { InMemoryMovieWithGenres(it).toModel() } ?: Movie(id = movieId)
        return movieFlows.getOrPut(movieId) { MutableStateFlow(initial) }
    }

    override suspend fun getAllGenres(): List<Genre> = genres.toList()

    override suspend fun getMovieWithRemoteKeys(movieId: Int): MovieRemoteKeysModel? =
        movieRemoteKeys[movieId]

    override suspend fun clearRemoteKeys() {
        movieRemoteKeys.clear()
    }

    override suspend fun insertAllRemoteKeys(localKeys: List<MovieRemoteKeysModel>) {
        localKeys.forEach { movieRemoteKeys[it.movieId] = it }
    }

    override suspend fun getTvShowWithRemoteKeys(tvShowId: Int): TvShowRemoteKeysModel? =
        tvShowRemoteKeys[tvShowId]

    override suspend fun clearTvShowRemoteKeys() {
        tvShowRemoteKeys.clear()
    }

    override suspend fun insertAllTvShowRemoteKeys(localKeys: List<TvShowRemoteKeysModel>) {
        localKeys.forEach { tvShowRemoteKeys[it.tvShowId] = it }
    }
}

private class InMemoryMovieWithGenres(private val movieDetail: MovieDetail) : GetMovieWithGenres {
    override fun toModel(): Movie = Movie(
        genreIds = movieDetail.genreIds,
        id = movieDetail.id,
        overview = movieDetail.overview,
        posterPath = movieDetail.posterPath,
        title = movieDetail.title,
        releaseDate = movieDetail.releaseDate,
        currentPage = movieDetail.currentPage,
        adult = movieDetail.adult,
        backdropPath = movieDetail.backdropPath,
        originalLanguage = movieDetail.originalLanguage,
        popularity = movieDetail.popularity,
        video = movieDetail.video,
        voteAverage = movieDetail.voteAverage,
        voteCount = movieDetail.voteCount,
        originalTitle = ""
    )
}

private class InMemoryTvShow(private val tvShowDetail: TvShowDetail) : GetTvShow {
    override fun toModel(): TvShow = TvShow(
        genreIds = tvShowDetail.genreIds,
        id = tvShowDetail.id,
        overview = tvShowDetail.overview,
        posterPath = tvShowDetail.posterPath,
        name = tvShowDetail.name,
        firstAirDate = tvShowDetail.firstAirDate,
        currentPage = tvShowDetail.currentPage,
        backdropPath = tvShowDetail.backdropPath,
        originalLanguage = tvShowDetail.originalLanguage,
        popularity = tvShowDetail.popularity,
        voteAverage = tvShowDetail.voteAverage,
        voteCount = tvShowDetail.voteCount,
        originalName = tvShowDetail.originalName
    )
}

private class InMemoryPagingSource<Value : Any>(
    private val itemsProvider: () -> List<Value>
) : PagingSource<Int, Value>() {
    override fun getRefreshKey(state: PagingState<Int, Value>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Value> {
        val allItems = itemsProvider()
        val page = when (val key = params.key) {
            null, 0 -> 1
            else -> key
        }
        val pageSize = params.loadSize
        val fromIndex = (page - 1) * pageSize
        val toIndex = minOf(fromIndex + pageSize, allItems.size)

        val pageData = if (fromIndex < allItems.size && fromIndex >= 0) {
            allItems.subList(fromIndex, toIndex)
        } else {
            emptyList()
        }

        val prevKey = if (page > 1) page - 1 else null
        val nextKey = if (toIndex < allItems.size) page + 1 else null

        return LoadResult.Page(
            data = pageData,
            prevKey = prevKey,
            nextKey = nextKey
        )
    }
}


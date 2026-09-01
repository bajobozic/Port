package com.bajobozic.storage.data.source

import androidx.paging.PagingSource
import androidx.room.Transaction
import com.bajobozic.storage.data.db.AppDatabase
import com.bajobozic.storage.data.entity.GenreEntity
import com.bajobozic.storage.data.entity.MovieEntity
import com.bajobozic.storage.data.entity.MovieRemoteKeys
import com.bajobozic.storage.data.entity.TvShowEntity
import com.bajobozic.storage.data.entity.TvShowRemoteKeys
import com.bajobozic.storage.data.entity.toModel
import com.bajobozic.storage.domain.model.Genre
import com.bajobozic.storage.domain.model.GetMovieWithGenres
import com.bajobozic.storage.domain.model.GetTvShow
import com.bajobozic.storage.domain.model.Movie
import com.bajobozic.storage.domain.model.MovieDetail
import com.bajobozic.storage.domain.model.MovieRemoteKeysModel
import com.bajobozic.storage.domain.model.TvShowDetail
import com.bajobozic.storage.domain.model.TvShowRemoteKeysModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class LocalDataSourceImpl(private val appDatabase: AppDatabase) : LocalDataSource {

    override suspend fun insertAllMovies(
        list: List<MovieDetail>,
        genreList: List<Genre>,
        genreIdsPerMovie: List<List<Genre>>
    ) {
        val movieEntities = list.map { it.toEntity() }
        val genreEntities = genreList.map { GenreEntity(id = it.id, name = it.name) }
        val genreIds = genreIdsPerMovie.map { genres -> genres.map { it.id } }
        appDatabase.getMovieDao().insertAll(movieEntities, genreEntities, genreIds)
    }

    override suspend fun deleteThenInsertAllMovies(
        list: List<MovieDetail>,
        genreList: List<Genre>,
        genreIdsPerMovie: List<List<Genre>>
    ) {
        val movieEntities = list.map { it.toEntity() }
        val genreEntities = genreList.map { GenreEntity(id = it.id, name = it.name) }
        val genreIds = genreIdsPerMovie.map { genres -> genres.map { it.id } }
        appDatabase.getMovieDao().deleteThenInsertAll(movieEntities, genreEntities, genreIds)
    }

    override suspend fun insertAllTvShows(list: List<TvShowDetail>) {
        appDatabase.getTvShowDao().insertTvShows(list.map { it.toEntity() })
    }

    override suspend fun deleteThenInsertAllTvShows(list: List<TvShowDetail>) {
        appDatabase.getTvShowDao().clearAll()
        appDatabase.getTvShowDao().insertTvShows(list.map { it.toEntity() })
    }

    override suspend fun deleteMovie(movieId: Int) {
        appDatabase.getMovieDao().deleteMovie(movieId)
    }

    override suspend fun clearAll() {
        appDatabase.getMovieDao().clearAll()
    }

    override suspend fun clearAllTvShows() {
        appDatabase.getTvShowDao().clearAll()
    }

    @Transaction
    override suspend fun batchTransaction(block: suspend () -> Unit) {
        block()
    }

    override suspend fun getMaxCurrentPage(): Int? {
        return appDatabase.getMovieDao().getMaxCurrentPage()
    }

    override suspend fun getTvShowMaxCurrentPage(): Int? {
        return appDatabase.getTvShowDao().getMaxCurrentPage()
    }

    @Suppress("UNCHECKED_CAST")
    override fun getPagingSource(): PagingSource<Int, GetMovieWithGenres> {
        return appDatabase.getMovieDao().pagingSource() as PagingSource<Int, GetMovieWithGenres>
    }

    @Suppress("UNCHECKED_CAST")
    override fun getTvShowPagingSource(): PagingSource<Int, GetTvShow> {
        return appDatabase.getTvShowDao().pagingSource() as PagingSource<Int, GetTvShow>
    }

    override fun getMovie(movieId: Int): Flow<Movie> {
        return appDatabase.getMovieDao().getMovieFlow(movieId).map { it.toModel() }
    }

    override suspend fun getAllGenres(): List<Genre> {
        return appDatabase.getMovieDao().getAllGenres().map { it.toModel() }
    }

    override suspend fun getMovieWithRemoteKeys(movieId: Int): MovieRemoteKeysModel? {
        return appDatabase.getMovieRemoteKeysDao().remoteKeysByMovieId(movieId)?.toModel()
    }

    override suspend fun clearRemoteKeys() {
        appDatabase.getMovieRemoteKeysDao().clearRemoteKeys()
    }

    override suspend fun insertAllRemoteKeys(localKeys: List<MovieRemoteKeysModel>) {
        appDatabase.getMovieRemoteKeysDao().insertAll(localKeys.map {
            MovieRemoteKeys(movieId = it.movieId, prevKey = it.prevKey, nextKey = it.nextKey)
        })
    }

    override suspend fun getTvShowWithRemoteKeys(tvShowId: Int): TvShowRemoteKeysModel? {
        return appDatabase.getTvShowRemoteKeysDao().remoteKeysByTvShowId(tvShowId)?.toModel()
    }

    override suspend fun clearTvShowRemoteKeys() {
        appDatabase.getTvShowRemoteKeysDao().clearRemoteKeys()
    }

    override suspend fun insertAllTvShowRemoteKeys(localKeys: List<TvShowRemoteKeysModel>) {
        appDatabase.getTvShowRemoteKeysDao().insertAll(localKeys.map {
            TvShowRemoteKeys(tvShowId = it.tvShowId, prevKey = it.prevKey, nextKey = it.nextKey)
        })
    }

    private fun MovieDetail.toEntity() = MovieEntity(
        id = id,
        adult = adult,
        backdropPath = backdropPath,
        originalLanguage = originalLanguage,
        originalTitle = "",
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        releaseDate = releaseDate,
        title = title,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount,
        currentPage = currentPage
    )

    private fun TvShowDetail.toEntity() = TvShowEntity(
        id = id,
        backdropPath = backdropPath,
        originalLanguage = originalLanguage,
        originalName = originalName,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        firstAirDate = firstAirDate,
        name = name,
        voteAverage = voteAverage,
        voteCount = voteCount,
        currentPage = currentPage
    )
}
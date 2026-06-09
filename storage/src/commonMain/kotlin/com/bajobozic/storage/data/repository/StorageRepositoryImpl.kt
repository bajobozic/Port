package com.bajobozic.storage.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import com.bajobozic.storage.data.entity.GenreEntity
import com.bajobozic.storage.data.entity.MovieEntity
import com.bajobozic.storage.data.entity.MovieRemoteKeys
import com.bajobozic.storage.data.entity.TvShowEntity
import com.bajobozic.storage.data.entity.TvShowRemoteKeys
import com.bajobozic.storage.data.entity.toModel
import com.bajobozic.storage.data.source.LocalDataSource
import com.bajobozic.storage.domain.model.Genre
import com.bajobozic.storage.domain.model.GetMovieWithGenres
import com.bajobozic.storage.domain.model.GetTvShow
import com.bajobozic.storage.domain.model.Movie
import com.bajobozic.storage.domain.model.MovieDetail
import com.bajobozic.storage.domain.model.MovieRemoteKeysModel
import com.bajobozic.storage.domain.model.TvShowDetail
import com.bajobozic.storage.domain.model.TvShowRemoteKeysModel
import com.bajobozic.storage.domain.repository.StorageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class StorageRepositoryImpl @OptIn(ExperimentalPagingApi::class) constructor(
    private val localDataSource: LocalDataSource
) : StorageRepository {
    @Suppress("UNCHECKED_CAST")
    override fun getPagingSource(): PagingSource<Int, GetMovieWithGenres> {
        return localDataSource.getPagingSource() as PagingSource<Int, GetMovieWithGenres>
    }

    @Suppress("UNCHECKED_CAST")
    override fun getTvShowPagingSource(): PagingSource<Int, GetTvShow> {
        return localDataSource.getTvShowPagingSource() as PagingSource<Int, GetTvShow>
    }


    override suspend fun getAllGenres(): List<Genre> {
        return localDataSource.getAllGenres().map { genreEntity -> genreEntity.toModel() }
    }

    override suspend fun getMaxCurrentPage(): Int {
        return localDataSource.getMaxCurrentPage() ?: 0
    }

    override suspend fun getTvShowMaxCurrentPage(): Int {
        return localDataSource.getTvShowMaxCurrentPage() ?: 0
    }

    override fun getMovie(movieId: Int): Flow<Movie> {
        return localDataSource.getMovie(movieId)
            .map { movieWithGenres -> movieWithGenres.toModel() }
    }

    override suspend fun insertAllMovies(
        list: List<MovieDetail>,
        genreList: List<Genre>,
        genreIdsPerMovie: List<List<Genre>>
    ) {
        localDataSource.insertAllMovies(
            list = list.map { movieDetail ->
                MovieEntity(
                    id = movieDetail.id,
                    adult = movieDetail.adult,
                    backdropPath = movieDetail.backdropPath,
                    originalLanguage = movieDetail.originalLanguage,
                    originalTitle = "", // MovieDetail doesn't have it
                    overview = movieDetail.overview,
                    popularity = movieDetail.popularity,
                    posterPath = movieDetail.posterPath,
                    releaseDate = movieDetail.releaseDate,
                    title = movieDetail.title,
                    video = movieDetail.video,
                    voteAverage = movieDetail.voteAverage,
                    voteCount = movieDetail.voteCount,
                    currentPage = movieDetail.currentPage
                )
            },
            genreList = genreList.map { genre -> GenreEntity(id = genre.id, name = genre.name) },
            genreIdsPerMovie = genreIdsPerMovie.map { genres -> genres.map { it.id } }
        )
    }

    override suspend fun deleteThenInsertAllMovies(
        list: List<MovieDetail>,
        genreList: List<Genre>,
        genreIdsPerMovie: List<List<Genre>>
    ) {
        localDataSource.deleteThenInsertAllMovies(
            list = list.map { movieDetail ->
                MovieEntity(
                    id = movieDetail.id,
                    adult = movieDetail.adult,
                    backdropPath = movieDetail.backdropPath,
                    originalLanguage = movieDetail.originalLanguage,
                    originalTitle = "", // MovieDetail doesn't have it
                    overview = movieDetail.overview,
                    popularity = movieDetail.popularity,
                    posterPath = movieDetail.posterPath,
                    releaseDate = movieDetail.releaseDate,
                    title = movieDetail.title,
                    video = movieDetail.video,
                    voteAverage = movieDetail.voteAverage,
                    voteCount = movieDetail.voteCount,
                    currentPage = movieDetail.currentPage
                )
            },
            genreList = genreList.map { genre -> GenreEntity(id = genre.id, name = genre.name) },
            genreIdsPerMovie = genreIdsPerMovie.map { genres -> genres.map { it.id } }
        )
    }

    override suspend fun insertAllTvShows(list: List<TvShowDetail>) {
        localDataSource.insertAllTvShows(
            list.map { tvShowDetail ->
                TvShowEntity(
                    id = tvShowDetail.id,
                    backdropPath = tvShowDetail.backdropPath,
                    originalLanguage = tvShowDetail.originalLanguage,
                    originalName = tvShowDetail.originalName,
                    overview = tvShowDetail.overview,
                    popularity = tvShowDetail.popularity,
                    posterPath = tvShowDetail.posterPath,
                    firstAirDate = tvShowDetail.firstAirDate,
                    name = tvShowDetail.name,
                    voteAverage = tvShowDetail.voteAverage,
                    voteCount = tvShowDetail.voteCount,
                    currentPage = tvShowDetail.currentPage
                )
            }
        )
    }

    override suspend fun deleteThenInsertAllTvShows(list: List<TvShowDetail>) {
        localDataSource.deleteThenInsertAllTvShows(
            list.map { tvShowDetail ->
                TvShowEntity(
                    id = tvShowDetail.id,
                    backdropPath = tvShowDetail.backdropPath,
                    originalLanguage = tvShowDetail.originalLanguage,
                    originalName = tvShowDetail.originalName,
                    overview = tvShowDetail.overview,
                    popularity = tvShowDetail.popularity,
                    posterPath = tvShowDetail.posterPath,
                    firstAirDate = tvShowDetail.firstAirDate,
                    name = tvShowDetail.name,
                    voteAverage = tvShowDetail.voteAverage,
                    voteCount = tvShowDetail.voteCount,
                    currentPage = tvShowDetail.currentPage
                )
            }
        )
    }

    override suspend fun deleteMovie(movieId: Int) {
        localDataSource.deleteMovie(movieId)
    }

    override suspend fun batchTransaction(block: suspend () -> Unit) {
        localDataSource.batchTransaction { block() }
    }

    override suspend fun getMovieWithRemoteKeys(movieId: Int): MovieRemoteKeysModel {
        return localDataSource.getMovieWithRemoteKeys(movieId)?.let { remoteKeysEntity ->
            return remoteKeysEntity.toModel()
        } ?: MovieRemoteKeysModel(
            movieId = movieId,
            prevKey = null,
            nextKey = null
        )
    }

    override suspend fun getTvShowWithRemoteKeys(tvShowId: Int): TvShowRemoteKeysModel {
        return localDataSource.getTvShowWithRemoteKeys(tvShowId)?.let { remoteKeysEntity ->
            return remoteKeysEntity.toModel()
        } ?: TvShowRemoteKeysModel(
            tvShowId = tvShowId,
            prevKey = null,
            nextKey = null
        )
    }

    override suspend fun clearRemoteKeys() {
        localDataSource.clearRemoteKeys()
    }

    override suspend fun clearTvShowRemoteKeys() {
        localDataSource.clearTvShowRemoteKeys()
    }

    override suspend fun insertAllRemoteKeys(keys: List<MovieRemoteKeysModel>) {
        val localKeys = keys.map { remoteKeyModel ->
            MovieRemoteKeys(
                movieId = remoteKeyModel.movieId,
                prevKey = remoteKeyModel.prevKey,
                nextKey = remoteKeyModel.nextKey
            )
        }
        localDataSource.insertAllRemoteKeys(localKeys)
    }

    override suspend fun insertAllTvShowRemoteKeys(keys: List<TvShowRemoteKeysModel>) {
        val localKeys = keys.map { remoteKeyModel ->
            TvShowRemoteKeys(
                tvShowId = remoteKeyModel.tvShowId,
                prevKey = remoteKeyModel.prevKey,
                nextKey = remoteKeyModel.nextKey
            )
        }
        localDataSource.insertAllTvShowRemoteKeys(localKeys)
    }
}
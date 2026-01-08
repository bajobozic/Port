package com.bajobozic.storage.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import com.bajobozic.storage.data.entity.GenreEntity
import com.bajobozic.storage.data.entity.MovieEntity
import com.bajobozic.storage.data.entity.MovieRemoteKeys
import com.bajobozic.storage.data.entity.toModel
import com.bajobozic.storage.data.source.LocalDataSource
import com.bajobozic.storage.domain.model.Genre
import com.bajobozic.storage.domain.model.GetMovieWithGenres
import com.bajobozic.storage.domain.model.Movie
import com.bajobozic.storage.domain.model.MovieDetail
import com.bajobozic.storage.domain.model.MovieRemoteKeysModel
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


    override suspend fun getAllGenres(): List<Genre> {
        return localDataSource.getAllGenres().map { genreEntity -> genreEntity.toModel() }
    }

    override suspend fun getMaxCurrentPage(): Int {
        return localDataSource.getMaxCurrentPage() ?: 0
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
                Movie(
                    id = movieDetail.id,
                    genreIds = movieDetail.genreIds
                        .map { genre -> Genre(id = genre.id, name = genre.name) },
                    overview = movieDetail.overview,
                    releaseDate = movieDetail.releaseDate,
                    posterPath = movieDetail.posterPath,
                    title = movieDetail.title,
                    currentPage = movieDetail.currentPage,
                    adult = movieDetail.adult,
                    backdropPath = movieDetail.backdropPath,
                    originalLanguage = movieDetail.originalLanguage,
                    popularity = movieDetail.popularity,
                    video = movieDetail.video,
                    voteAverage = movieDetail.voteAverage,
                    voteCount = movieDetail.voteCount,
                )
            }
                .map { movie ->
                    MovieEntity(
                        id = movie.id,
                        overview = movie.overview,
                        releaseDate = movie.releaseDate,
                        posterPath = movie.posterPath,
                        title = movie.title,
                        currentPage = movie.currentPage,
                        adult = movie.adult,
                        backdropPath = movie.backdropPath,
                        originalLanguage = movie.originalLanguage,
                        popularity = movie.popularity,
                        video = movie.video,
                        voteAverage = movie.voteAverage,
                        voteCount = movie.voteCount,
                        originalTitle = movie.originalTitle
                    )
                },
            genreList = genreList.map { genre -> GenreEntity(id = genre.id, name = genre.name) },
            genreIdsPerMovie = genreIdsPerMovie.map({ genres -> genres.map { genre -> genre.id } })
        )
    }

    override suspend fun deleteThenInsertAllMovies(
        list: List<MovieDetail>,
        genreList: List<Genre>,
        genreIdsPerMovie: List<List<Genre>>
    ) {
        localDataSource.deleteThenInsertAllMovies(
            list = list.map { movieDetail ->
                Movie(
                    id = movieDetail.id,
                    genreIds = movieDetail.genreIds
                        .map { genre -> Genre(id = genre.id, name = genre.name) },
                    overview = movieDetail.overview,
                    releaseDate = movieDetail.releaseDate,
                    posterPath = movieDetail.posterPath,
                    title = movieDetail.title,
                    currentPage = movieDetail.currentPage,
                    adult = movieDetail.adult,
                    backdropPath = movieDetail.backdropPath,
                    originalLanguage = movieDetail.originalLanguage,
                    popularity = movieDetail.popularity,
                    video = movieDetail.video,
                    voteAverage = movieDetail.voteAverage,
                    voteCount = movieDetail.voteCount,
                )
            }
                .map { movie ->
                    MovieEntity(
                        id = movie.id,
                        overview = movie.overview,
                        releaseDate = movie.releaseDate,
                        posterPath = movie.posterPath,
                        title = movie.title,
                        currentPage = movie.currentPage,
                        adult = movie.adult,
                        backdropPath = movie.backdropPath,
                        originalLanguage = movie.originalLanguage,
                        popularity = movie.popularity,
                        video = movie.video,
                        voteAverage = movie.voteAverage,
                        voteCount = movie.voteCount,
                        originalTitle = movie.originalTitle
                    )
                },
            genreList = genreList.map { genre -> GenreEntity(id = genre.id, name = genre.name) },
            genreIdsPerMovie = genreIdsPerMovie.map({ genres -> genres.map { genre -> genre.id } })
        )
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

    override suspend fun clearRemoteKeys() {
        localDataSource.clearRemoteKeys()
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

}
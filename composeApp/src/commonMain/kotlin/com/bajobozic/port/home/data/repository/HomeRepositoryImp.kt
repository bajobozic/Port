package com.bajobozic.port.home.data.repository


import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.map
import com.bajobozic.port.home.data.locale.HomeLocalDataSource
import com.bajobozic.port.home.data.remote.client.HomeRemoteDataSource
import com.bajobozic.port.home.data.remote.client.MovieRemoteMediator
import com.bajobozic.port.home.data.remote.dto.initKeys
import com.bajobozic.port.home.data.remote.dto.toEntity
import com.bajobozic.port.home.domain.ErrorHandler
import com.bajobozic.port.home.domain.model.GetMoviesWithGenres
import com.bajobozic.port.home.domain.model.Movie
import com.bajobozic.port.home.domain.model.MovieDetail
import com.bajobozic.port.home.domain.repository.HomeRepository
import home.data.local.db.toModel
import home.data.local.db.toModelDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge


@OptIn(ExperimentalPagingApi::class)
internal class HomeRepositoryImp(
    private val movieRemoteMediatorFactory: MovieRemoteMediator,
    private val homeRemoteDataSource: HomeRemoteDataSource,
    private val homeLocalDataSource: HomeLocalDataSource,
    private val errorHandler: ErrorHandler
) : HomeRepository {

    override suspend fun deleteMovie(movieId: Int) {
        try {
            homeLocalDataSource.deleteMovie(movieId)
        } catch (t: Throwable) {
//            Log.d("ToDeleteApp", "deleteMovie: ${t.message.orEmpty()}")
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : GetMoviesWithGenres> getPagingSource(): PagingSource<Int, GetMoviesWithGenres> {
        return homeLocalDataSource.getPagingSource() as PagingSource<Int, GetMoviesWithGenres>
    }

    override fun getPagingData(language: String): Flow<PagingData<Movie>> {

        val pager = Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 5, initialLoadSize = 20),
            initialKey = 0,
            pagingSourceFactory = {
                homeLocalDataSource.getPagingSource()
            },
            remoteMediator = movieRemoteMediatorFactory
        )
        return pager.flow.map { pagingData -> pagingData.map { movieWithGenres -> movieWithGenres.toModel() } }
    }

    override suspend fun getMovieDetail(movieId: Int, language: String): Flow<MovieDetail> {
        val movieFunction = suspend {
            homeRemoteDataSource.getMovieWithRelationships(language, movieId)
        }
        return merge(
            homeLocalDataSource.getMovie(movieId).distinctUntilChanged()
                .map { it.toModelDetail() },//this will be emitted instantly
            movieFunction.asFlow()//this is just to update database in case something change while navigating
                .map { movieGenresDto ->
                    homeLocalDataSource.insertAllMovies(
                        movieGenresDto.listOne.map {
                            it.initKeys(
                                homeLocalDataSource.getMovie(
                                    movieId
                                ).distinctUntilChanged()
                                    .first().movie.currentPage!!
                            )
                        }.map { it.toEntity() },
                        movieGenresDto.listTwo.map { it.toEntity() },
                        movieGenresDto.listThree
                    )
                }.filterIsInstance(MovieDetail::class)//we are not interested in emission
        )
            .catch { errorHandler.handleError(it) }
    }

}
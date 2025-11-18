package com.bajobozic.port.home.data.repository


import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.RemoteMediator
import androidx.paging.map
import com.bajobozic.port.home.data.locale.HomeLocalDataSource
import com.bajobozic.port.home.data.locale.entity.MovieWithGenres
import com.bajobozic.port.home.data.locale.entity.toModel
import com.bajobozic.port.home.data.remote.client.HomeRemoteDataSource
import com.bajobozic.port.home.data.remote.dto.toMovieDetail
import com.bajobozic.port.home.data.remote.dto.toMovieVideo
import com.bajobozic.port.home.domain.ErrorHandler
import com.bajobozic.port.home.domain.model.GetMoviesWithGenres
import com.bajobozic.port.home.domain.model.Movie
import com.bajobozic.port.home.domain.model.MovieDetail
import com.bajobozic.port.home.domain.model.MovieVideo
import com.bajobozic.port.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


@OptIn(ExperimentalPagingApi::class)
internal class HomeRepositoryImp(
    private val movieRemoteMediatorFactory: RemoteMediator<Int, MovieWithGenres>,
    private val homeRemoteDataSource: HomeRemoteDataSource,
    private val homeLocalDataSource: HomeLocalDataSource,
    private val errorHandler: ErrorHandler
) : HomeRepository {

    override suspend fun deleteMovie(movieId: Int) {
        try {
            homeLocalDataSource.deleteMovie(movieId)
        } catch (t: Throwable) {
            println(t.message.orEmpty())
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

    override suspend fun getMovieDetail(movieId: Int, language: String): MovieDetail {
        return homeRemoteDataSource.getMovie(language, movieId)
            .toMovieDetail()
    }

    override suspend fun getMovieVideo(
        id: Int,
        language: String
    ): List<MovieVideo> {
        return try {
            homeRemoteDataSource.getMovieVideos(language, id).map { dto -> dto.toMovieVideo() }
        } catch (t: Throwable) {
            println(t.message.orEmpty())
            emptyList<MovieVideo>()
        }
    }

}
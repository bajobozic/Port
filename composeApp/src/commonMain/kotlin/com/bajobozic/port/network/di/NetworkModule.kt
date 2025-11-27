package com.bajobozic.port.network.di

import com.bajobozic.port.network.data.client.ApiClient
import com.bajobozic.port.network.data.client.MoviesApiClient
import com.bajobozic.port.network.data.client.RemoteDataSource
import com.bajobozic.port.network.data.client.RemoteDataSourceImpl
import com.bajobozic.port.network.data.client.createHttpClient
import com.bajobozic.port.network.data.repository.NetworkRepositoryImpl
import com.bajobozic.port.network.data.util.NetworkErrorHandler
import com.bajobozic.port.network.domain.repository.NetworkRepository
import com.bajobozic.port.network.domain.usecase.GetGenresUseCase
import com.bajobozic.port.network.domain.usecase.GetMovieDetailUseCase
import com.bajobozic.port.network.domain.usecase.GetMovieVideoUseCase
import com.bajobozic.port.network.domain.usecase.GetMoviesUseCase
import com.bajobozic.port.shared_component.domain.ErrorHandler
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val networkModule = module {
    single<ApiClient> {
        MoviesApiClient(createHttpClient(get()))
    }
    singleOf(::NetworkErrorHandler).bind<ErrorHandler>()
    singleOf(::RemoteDataSourceImpl).bind<RemoteDataSource>()
    singleOf(::NetworkRepositoryImpl).bind<NetworkRepository>()
    single<GetMovieDetailUseCase> { GetMovieDetailUseCase(get<NetworkRepository>()::getMovieDetail) }
    single<GetMovieVideoUseCase> { GetMovieVideoUseCase(get<NetworkRepository>()::getMovieVideos) }
    single<GetGenresUseCase> { GetGenresUseCase(get<NetworkRepository>()::getGenres) }
    single<GetMoviesUseCase> { GetMoviesUseCase(get<NetworkRepository>()::getMovies) }
}
package com.bajobozic.network.di

import com.bajobozic.network.data.client.ApiClient
import com.bajobozic.network.data.client.MoviesApiClient
import com.bajobozic.network.data.client.RemoteDataSource
import com.bajobozic.network.data.client.RemoteDataSourceImpl
import com.bajobozic.network.data.client.createHttpClient
import com.bajobozic.network.data.repository.NetworkRepositoryImpl
import com.bajobozic.network.data.util.NetworkErrorHandler
import com.bajobozic.network.domain.repository.NetworkRepository
import com.bajobozic.network.domain.usecase.GetGenresUseCase
import com.bajobozic.network.domain.usecase.GetMovieDetailUseCase
import com.bajobozic.network.domain.usecase.GetMovieVideoUseCase
import com.bajobozic.network.domain.usecase.GetMoviesUseCase
import com.bajobozic.shared_component.ErrorHandler
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val protectedNetworkModule = module {
    single<ApiClient> {
        MoviesApiClient(
            createHttpClient(get())
        )
    }
    singleOf(::NetworkErrorHandler).bind<ErrorHandler>()
    singleOf(::RemoteDataSourceImpl).bind<RemoteDataSource>()
    singleOf(::NetworkRepositoryImpl).bind<NetworkRepository>()
}

val networkModule = module {
    includes(protectedNetworkModule)
    single<GetMovieDetailUseCase> {
        GetMovieDetailUseCase(get<NetworkRepository>()::getMovieDetail)
    }
    single<GetMovieVideoUseCase> {
        GetMovieVideoUseCase(
            get<NetworkRepository>()::getMovieVideos
        )
    }
    single<GetGenresUseCase> {
        GetGenresUseCase(
            get<NetworkRepository>()::getGenres
        )
    }
    single<GetMoviesUseCase> {
        GetMoviesUseCase(
            get<NetworkRepository>()::getMovies
        )
    }
}

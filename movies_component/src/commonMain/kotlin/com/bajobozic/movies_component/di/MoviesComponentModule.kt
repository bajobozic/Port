package com.bajobozic.movies_component.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.RemoteMediator
import com.bajobozic.movies_component.data.mediator.MovieRemoteMediator
import com.bajobozic.movies_component.data.repository.MoviesRepositoryImpl
import com.bajobozic.movies_component.domain.repository.MoviesRepository
import com.bajobozic.movies_component.domain.usecase.GetMoviesPagingDataUseCase
import com.bajobozic.storage.domain.model.GetMovieWithGenres
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

@OptIn(ExperimentalPagingApi::class)
internal val protectedMoviesComponentModule = module {
    single<MoviesRepository> { MoviesRepositoryImpl(get(named("movieRemoteMediator")), get()) }
    single(named("movieRemoteMediator")) {
        MovieRemoteMediator(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }.bind<RemoteMediator<Int, GetMovieWithGenres>>()
    single<GetMoviesPagingDataUseCase> { GetMoviesPagingDataUseCase(get<MoviesRepository>()::getPagingData) }
}

val moviesComponentModule = module {
    includes(protectedMoviesComponentModule)
}

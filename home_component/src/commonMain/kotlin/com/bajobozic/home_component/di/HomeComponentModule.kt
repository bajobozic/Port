package com.bajobozic.home_component.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.RemoteMediator
import com.bajobozic.home_component.data.mediator.MovieRemoteMediator
import com.bajobozic.home_component.data.repository.HomeRepositoryImp
import com.bajobozic.home_component.domain.repository.HomeRepository
import com.bajobozic.home_component.domain.usecase.GetPagingDataUseCase
import com.bajobozic.storage.domain.model.GetMovieWithGenres
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

@OptIn(ExperimentalPagingApi::class)
internal val protectedHomeComponentModule = module {
    singleOf(::HomeRepositoryImp).bind<HomeRepository>()
    singleOf(::MovieRemoteMediator).bind<RemoteMediator<Int, GetMovieWithGenres>>()
    single<GetPagingDataUseCase> { GetPagingDataUseCase(get<HomeRepository>()::getPagingData) }
}

val homeComponentModule = module {
    includes(protectedHomeComponentModule)
}
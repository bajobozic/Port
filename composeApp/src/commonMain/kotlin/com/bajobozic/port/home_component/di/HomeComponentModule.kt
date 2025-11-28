package com.bajobozic.port.home_component.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.RemoteMediator
import com.bajobozic.port.home_component.data.mediator.MovieRemoteMediator
import com.bajobozic.port.home_component.data.repository.HomeRepositoryImp
import com.bajobozic.port.home_component.domain.repository.HomeRepository
import com.bajobozic.port.storage.domain.model.GetMovieWithGenres
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

@OptIn(ExperimentalPagingApi::class)
val homeComponentModule = module {
    singleOf(::HomeRepositoryImp).bind<HomeRepository>()
    singleOf(::MovieRemoteMediator).bind<RemoteMediator<Int, GetMovieWithGenres>>()
}
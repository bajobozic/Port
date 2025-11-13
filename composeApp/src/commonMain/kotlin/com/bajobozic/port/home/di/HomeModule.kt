package com.bajobozic.port.home.di

import com.bajobozic.port.home.data.locale.HomeLocalDataSource
import com.bajobozic.port.home.data.locale.HomeLocalDataSourceImpl
import com.bajobozic.port.home.data.remote.GlobalErrorHandler
import com.bajobozic.port.home.data.remote.client.HomeRemoteDataSource
import com.bajobozic.port.home.data.remote.client.HomeRemoteDataSourceImpl
import com.bajobozic.port.home.data.remote.client.MovieRemoteMediator
import com.bajobozic.port.home.data.repository.HomeRepositoryImp
import com.bajobozic.port.home.domain.ErrorHandler
import com.bajobozic.port.home.domain.repository.HomeRepository
import com.bajobozic.port.home.presentation.HomeViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val homeModule = module {
    singleOf(::GlobalErrorHandler).bind<ErrorHandler>()
    singleOf(::HomeLocalDataSourceImpl).bind<HomeLocalDataSource>()
    singleOf(::HomeRemoteDataSourceImpl).bind<HomeRemoteDataSource>()
    singleOf(::HomeRepositoryImp).bind<HomeRepository>()
    factory { MovieRemoteMediator(get(), get()) }
    viewModelOf(::HomeViewModel)
}

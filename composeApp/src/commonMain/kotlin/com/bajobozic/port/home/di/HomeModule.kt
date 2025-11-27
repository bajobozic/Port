package com.bajobozic.port.home.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.RemoteMediator
import com.bajobozic.port.home.data.remote.GlobalErrorHandler
import com.bajobozic.port.home.presentation.HomeViewModel
import com.bajobozic.port.home_component.data.mediator.MovieRemoteMediator
import com.bajobozic.port.home_component.data.repository.HomeRepositoryImp
import com.bajobozic.port.home_component.domain.repository.HomeRepository
import com.bajobozic.port.map.presentation.MapsViewModel
import com.bajobozic.port.network.data.client.RemoteDataSource
import com.bajobozic.port.network.data.client.RemoteDataSourceImpl
import com.bajobozic.port.shared_component.domain.ErrorHandler
import com.bajobozic.port.signin.presentation.SignInViewModel
import com.bajobozic.port.storage.data.source.LocalDataSource
import com.bajobozic.port.storage.data.source.LocalDataSourceImpl
import com.bajobozic.port.storage.domain.model.Movie
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

@OptIn(ExperimentalPagingApi::class)
val homeModule = module {
    singleOf(::GlobalErrorHandler).bind<ErrorHandler>()
    singleOf(::LocalDataSourceImpl).bind<LocalDataSource>()
    singleOf(::RemoteDataSourceImpl).bind<RemoteDataSource>()
    singleOf(::HomeRepositoryImp).bind<HomeRepository>()
    singleOf(::MovieRemoteMediator) { bind<RemoteMediator<Int, Movie>>() }
    viewModelOf(::HomeViewModel)
    viewModelOf(::SignInViewModel)
    viewModelOf(::MapsViewModel)
}

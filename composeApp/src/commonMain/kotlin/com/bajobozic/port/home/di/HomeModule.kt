package com.bajobozic.port.home.di

import androidx.paging.ExperimentalPagingApi
import com.bajobozic.port.home.presentation.HomeViewModel
import com.bajobozic.port.home_component.data.repository.HomeRepositoryImp
import com.bajobozic.port.home_component.domain.repository.HomeRepository
import com.bajobozic.port.home_component.domain.usecase.GetPagingDataUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

@OptIn(ExperimentalPagingApi::class)
val homeModule = module {
    singleOf(::HomeRepositoryImp).bind<HomeRepository>()
    single<GetPagingDataUseCase> { GetPagingDataUseCase(get<HomeRepository>()::getPagingData) }
    viewModelOf(::HomeViewModel)
}

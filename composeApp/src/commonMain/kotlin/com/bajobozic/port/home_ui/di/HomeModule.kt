package com.bajobozic.port.home_ui.di

import androidx.paging.ExperimentalPagingApi
import com.bajobozic.port.home_ui.presentation.HomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

@OptIn(ExperimentalPagingApi::class)
val homeModule = module {
    /*singleOf(::HomeRepositoryImp).bind<HomeRepository>()
    single<GetPagingDataUseCase> { GetPagingDataUseCase(get<HomeRepository>()::getPagingData) }*/
    viewModelOf(::HomeViewModel)
}

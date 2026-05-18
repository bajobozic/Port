package com.bajobozic.tv_component.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.RemoteMediator
import com.bajobozic.storage.domain.model.GetTvShow
import com.bajobozic.tv_component.data.mediator.TvShowRemoteMediator
import com.bajobozic.tv_component.data.repository.TvRepositoryImpl
import com.bajobozic.tv_component.domain.repository.TvRepository
import com.bajobozic.tv_component.domain.usecase.GetTvShowPagingDataUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

@OptIn(ExperimentalPagingApi::class)
internal val protectedTvComponentModule = module {
    factory(named("tvShowRemoteMediator")) {
        TvShowRemoteMediator(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }.bind<RemoteMediator<Int, GetTvShow>>()
    single<TvRepository> { TvRepositoryImpl(get(named("tvShowRemoteMediator")), get()) }
    single<GetTvShowPagingDataUseCase> { GetTvShowPagingDataUseCase(get<TvRepository>()::getPagingData) }
}

val tvComponentModule = module {
    includes(protectedTvComponentModule)
}

package com.bajobozic.storage.di

import androidx.paging.ExperimentalPagingApi
import com.bajobozic.storage.data.db.AppDatabase
import com.bajobozic.storage.data.db.MovieDao
import com.bajobozic.storage.data.repository.StorageRepositoryImpl
import com.bajobozic.storage.data.source.LocalDataSource
import com.bajobozic.storage.data.source.LocalDataSourceImpl
import com.bajobozic.storage.databaseModule
import com.bajobozic.storage.domain.repository.StorageRepository
import com.bajobozic.storage.domain.usecase.BatchTransactionUseCase
import com.bajobozic.storage.domain.usecase.ClearRemoteKeysUseCase
import com.bajobozic.storage.domain.usecase.DeleteThenInsertAllMoviesUseCase
import com.bajobozic.storage.domain.usecase.GetAllGenresUseCase
import com.bajobozic.storage.domain.usecase.GetMaxCurrentPageUseCase
import com.bajobozic.storage.domain.usecase.GetPagingSourceUseCase
import com.bajobozic.storage.domain.usecase.GetRemoteKeysByMovieIdUseCase
import com.bajobozic.storage.domain.usecase.InsertAllMoviesUseCase
import com.bajobozic.storage.domain.usecase.RemoteKeysInsertAllUseCase

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val protectedStorageModule = module {
    single<MovieDao> { get<AppDatabase>().getMovieDao() }
    singleOf(::LocalDataSourceImpl).bind<LocalDataSource>()
    singleOf(::StorageRepositoryImpl).bind<StorageRepository>()
}

@OptIn(ExperimentalPagingApi::class)
val storageModule = module {
    includes(databaseModule())
    includes(protectedStorageModule)
    single<GetAllGenresUseCase> { GetAllGenresUseCase(get<StorageRepository>()::getAllGenres) }
    single<DeleteThenInsertAllMoviesUseCase> { DeleteThenInsertAllMoviesUseCase(get<StorageRepository>()::deleteThenInsertAllMovies) }
    single<InsertAllMoviesUseCase> { InsertAllMoviesUseCase(get<StorageRepository>()::insertAllMovies) }
    single<GetMaxCurrentPageUseCase> { GetMaxCurrentPageUseCase(get<StorageRepository>()::getMaxCurrentPage) }
    single<BatchTransactionUseCase> { BatchTransactionUseCase(get<StorageRepository>()::batchTransaction) }
    single<GetRemoteKeysByMovieIdUseCase> { GetRemoteKeysByMovieIdUseCase(get<StorageRepository>()::getMovieWithRemoteKeys) }
    single<ClearRemoteKeysUseCase> { ClearRemoteKeysUseCase(get<StorageRepository>()::clearRemoteKeys) }
    single<RemoteKeysInsertAllUseCase> { RemoteKeysInsertAllUseCase(get<StorageRepository>()::insertAllRemoteKeys) }
    factory<GetPagingSourceUseCase> { GetPagingSourceUseCase(get<StorageRepository>()::getPagingSource) }
}
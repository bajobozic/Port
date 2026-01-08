package com.bajobozic.port.storage.di

import androidx.paging.ExperimentalPagingApi
import com.bajobozic.port.network.data.client.RemoteDataSource
import com.bajobozic.port.network.data.client.RemoteDataSourceImpl
import com.bajobozic.port.storage.data.db.AppDatabase
import com.bajobozic.port.storage.data.db.MovieDao
import com.bajobozic.port.storage.data.repository.StorageRepositoryImpl
import com.bajobozic.port.storage.data.source.LocalDataSource
import com.bajobozic.port.storage.data.source.LocalDataSourceImpl
import com.bajobozic.port.storage.domain.repository.StorageRepository
import com.bajobozic.port.storage.domain.usecase.BatchTransactionUseCase
import com.bajobozic.port.storage.domain.usecase.ClearRemoteKeysUseCase
import com.bajobozic.port.storage.domain.usecase.DeleteThenInsertAllMoviesUseCase
import com.bajobozic.port.storage.domain.usecase.GetAllGenresUseCase
import com.bajobozic.port.storage.domain.usecase.GetMaxCurrentPageUseCase
import com.bajobozic.port.storage.domain.usecase.GetPagingSourceUseCase
import com.bajobozic.port.storage.domain.usecase.GetRemoteKeysByMovieIdUseCase
import com.bajobozic.port.storage.domain.usecase.InsertAllMoviesUseCase
import com.bajobozic.port.storage.domain.usecase.RemoteKeysInsertAllUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val protectedStorageModule = module {
    single<MovieDao> { get<AppDatabase>().getMovieDao() }
    singleOf(::LocalDataSourceImpl).bind<LocalDataSource>()
    singleOf(::RemoteDataSourceImpl).bind<RemoteDataSource>()
    singleOf(::StorageRepositoryImpl).bind<StorageRepository>()
}

@OptIn(ExperimentalPagingApi::class)
val storageModule = module {
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
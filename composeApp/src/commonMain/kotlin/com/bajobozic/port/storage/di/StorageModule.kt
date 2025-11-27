package com.bajobozic.port.storage.di

import com.bajobozic.port.storage.data.db.AppDatabase
import com.bajobozic.port.storage.data.db.MovieDao
import com.bajobozic.port.storage.domain.repository.StorageRepository
import com.bajobozic.port.storage.domain.usecase.DeleteThenInsertAllMoviesUseCase
import com.bajobozic.port.storage.domain.usecase.GetAllGenresUseCase
import org.koin.dsl.module

internal val storageModule = module {
    single<MovieDao> { get<AppDatabase>().getMovieDao() }
    single<GetAllGenresUseCase> { GetAllGenresUseCase(get<StorageRepository>()::getAllGenres) }
    single<DeleteThenInsertAllMoviesUseCase> { DeleteThenInsertAllMoviesUseCase(get<StorageRepository>()::deleteThenInsertAllMovies) }
}
package com.bajobozic.port.home.di

import com.bajobozic.port.home.data.locale.db.AppDatabase
import com.bajobozic.port.home.data.locale.db.MovieDao
import com.bajobozic.port.home.data.remote.client.ApiClient
import com.bajobozic.port.home.data.remote.client.MoviesApiClient
import com.bajobozic.port.home.data.remote.client.createHttpClient
import org.koin.core.module.Module
import org.koin.dsl.module

fun commonModule(): Module = module {
    single<ApiClient> {
        MoviesApiClient(createHttpClient(get()))
    }
    single<MovieDao> { get<AppDatabase>().getMovieDao() }
}
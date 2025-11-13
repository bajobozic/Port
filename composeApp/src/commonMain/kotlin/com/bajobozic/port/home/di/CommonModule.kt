package com.bajobozic.port.home.di

import com.bajobozic.port.home.data.locale.db.AppDatabase
import com.bajobozic.port.home.data.locale.db.MovieDao
import org.koin.core.module.Module
import org.koin.dsl.module

fun commonModule(): Module = module {
    single<MovieDao> { get<AppDatabase>().getMovieDao() }
}
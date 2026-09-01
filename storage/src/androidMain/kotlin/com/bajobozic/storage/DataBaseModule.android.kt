package com.bajobozic.storage

import com.bajobozic.storage.data.db.AppDatabase
import com.bajobozic.storage.data.db.MovieDao
import com.bajobozic.storage.data.db.getRoomDatabase
import com.bajobozic.storage.data.source.LocalDataSource
import com.bajobozic.storage.data.source.LocalDataSourceImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal actual fun databaseModule(): Module = module {
    single<AppDatabase> {
        val builder = getDatabaseBuilder(context = get())
        getRoomDatabase(builder)
    }
    single<MovieDao> { get<AppDatabase>().getMovieDao() }
    singleOf(::LocalDataSourceImpl).bind<LocalDataSource>()
}
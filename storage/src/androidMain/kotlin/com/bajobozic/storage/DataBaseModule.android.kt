package com.bajobozic.storage

import com.bajobozic.storage.data.db.AppDatabase
import com.bajobozic.storage.data.db.getRoomDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual fun databaseModule(): Module = module {
    single<AppDatabase> {
        val builder = getDatabaseBuilder(context = get())
        getRoomDatabase(builder)
    }
}
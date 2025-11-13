package com.bajobozic.port

import com.bajobozic.port.home.data.locale.db.AppDatabase
import com.bajobozic.port.home.data.locale.db.getRoomDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single<AppDatabase> {
        val builder = getDatabaseBuilder(context = get())
        getRoomDatabase(builder)
    }
}
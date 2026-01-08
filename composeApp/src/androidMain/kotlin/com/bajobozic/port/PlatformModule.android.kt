package com.bajobozic.port


import com.bajobozic.storage.data.db.AppDatabase
import com.bajobozic.storage.data.db.getRoomDatabase
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single<AppDatabase> {
        val builder = getDatabaseBuilder(context = get())
        getRoomDatabase(builder)
    }

    single<HttpClientEngine> {
        OkHttp.create()
    }
}
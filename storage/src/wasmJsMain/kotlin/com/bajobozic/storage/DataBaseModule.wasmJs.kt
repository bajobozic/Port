package com.bajobozic.storage

import com.bajobozic.storage.data.source.LocalDataSource
import com.bajobozic.storage.data.source.WasmLocalDataSourceImpl
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

internal actual fun databaseModule(): Module = module {
    single { WasmLocalDataSourceImpl() }.bind<LocalDataSource>()
}

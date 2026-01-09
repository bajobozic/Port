package com.bajobozic.map_ui.di

import com.bajobozic.map_ui.presentation.MapsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val internalMapModule = module {
    viewModelOf(::MapsViewModel)
}
val mapModule = module {
    includes(internalMapModule)
}
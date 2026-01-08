package com.bajobozic.port.map_ui.di

import com.bajobozic.port.map_ui.presentation.MapsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val mapModule = module {
    viewModelOf(::MapsViewModel)
}
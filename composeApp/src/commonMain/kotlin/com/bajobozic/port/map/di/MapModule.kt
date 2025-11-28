package com.bajobozic.port.map.di

import com.bajobozic.port.map.presentation.MapsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val mapModule = module {
    viewModelOf(::MapsViewModel)
}
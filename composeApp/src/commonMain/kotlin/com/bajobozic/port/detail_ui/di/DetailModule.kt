package com.bajobozic.port.detail_ui.di

import com.bajobozic.port.detail_ui.presentation.DetailViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val detailModule = module {
    viewModel { params -> DetailViewModel(params.get(), get(), get()) }
}
package com.bajobozic.port.detail.di

import com.bajobozic.port.detail.presentation.DetailViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val detailModule = module {
    viewModel { params -> DetailViewModel(params.get(), get(), get()) }
}
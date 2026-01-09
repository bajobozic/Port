package com.bajobozic.detail_ui.di

import com.bajobozic.detail_ui.presentation.DetailViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

internal val protectedDetailModule = module {
    viewModel { params -> DetailViewModel(params.get(), get(), get()) }
}
val detailModule = module {
    includes(protectedDetailModule)
}
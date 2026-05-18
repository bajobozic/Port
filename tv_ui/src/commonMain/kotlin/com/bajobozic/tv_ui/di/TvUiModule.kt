package com.bajobozic.tv_ui.di

import com.bajobozic.tv_ui.presentation.TvShowsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val tvUiModule = module {
    viewModelOf(::TvShowsViewModel)
}

package com.bajobozic.movies_ui.di

import androidx.paging.ExperimentalPagingApi
import com.bajobozic.movies_ui.presentation.MoviesViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

@OptIn(ExperimentalPagingApi::class)
val moviesModule = module {
    viewModelOf(::MoviesViewModel)
}

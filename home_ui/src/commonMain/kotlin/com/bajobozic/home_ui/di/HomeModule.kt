package com.bajobozic.home_ui.di

import androidx.paging.ExperimentalPagingApi
import com.bajobozic.home_ui.presentation.HomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

@OptIn(ExperimentalPagingApi::class)
val homeModule = module {
    viewModelOf(::HomeViewModel)
}

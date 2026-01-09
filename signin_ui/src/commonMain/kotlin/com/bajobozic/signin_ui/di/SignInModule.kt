package com.bajobozic.signin_ui.di

import com.bajobozic.signin_ui.presentation.SignInViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val signInModule = module {
    viewModelOf(::SignInViewModel)
}
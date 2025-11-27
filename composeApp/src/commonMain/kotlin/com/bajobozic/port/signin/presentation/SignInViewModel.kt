package com.bajobozic.port.signin.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SignInViewModel : ViewModel() {
    private var _signInState = MutableStateFlow(SignInUiState(isLoading = false))
    val signInState = _signInState.asStateFlow()
}
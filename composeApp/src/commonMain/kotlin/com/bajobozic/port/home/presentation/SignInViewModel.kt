package com.bajobozic.port.home.presentation

import androidx.lifecycle.ViewModel
import com.bajobozic.port.home.presentation.component.SignInUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SignInViewModel : ViewModel() {
    private var _signInState = MutableStateFlow(SignInUiState(isLoading = false))
    val signInState = _signInState.asStateFlow()
}
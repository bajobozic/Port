package com.bajobozic.signin_ui

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun PlatformButton(modifier: Modifier, label: String, onClick: () -> Unit) {
    Button(modifier = modifier, onClick = onClick) {
        Text(label)
    }
}
package com.bajobozic.port

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun PlatformButton(modifier: Modifier, label: String, onClick: () -> Unit)
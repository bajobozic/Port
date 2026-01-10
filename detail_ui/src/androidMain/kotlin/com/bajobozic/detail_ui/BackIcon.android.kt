package com.bajobozic.detail_ui

import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.vectorResource
import port.detail_ui.generated.resources.Res
import port.detail_ui.generated.resources.arrow_back_android

@Composable
actual fun BackIcon(event: () -> Unit) {
    Icon(modifier = Modifier.clickable(onClick = {
        event()
    }), imageVector = vectorResource(Res.drawable.arrow_back_android), contentDescription = "Back")
}
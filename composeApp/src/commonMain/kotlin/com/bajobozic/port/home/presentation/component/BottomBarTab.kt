package com.bajobozic.port.home.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun BottomBarTab(
    modifier: Modifier,
    drawableResource: DrawableResource,
    title: String,
    onClick: () -> Unit
) {
    Column(modifier = modifier.clickable { onClick() }) {
        Icon(
            modifier = Modifier.fillMaxWidth(),
            imageVector = vectorResource(drawableResource),
            contentDescription = null
        )
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = title,
        )
    }
}
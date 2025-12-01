package com.bajobozic.port

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.graphics.toColorInt
import com.mmk.kmpnotifier.permission.permissionUtil

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark("#801b1b1b".toColorInt()))
        super.onCreate(savedInstanceState)
        val permissionUtil by permissionUtil()
        permissionUtil.askNotificationPermission() //this will ask permission in Android 13(API Level 33) or above, otherwise permission will be granted.
        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
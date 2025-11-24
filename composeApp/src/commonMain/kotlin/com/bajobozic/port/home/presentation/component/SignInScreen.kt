package com.bajobozic.port.home.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.bajobozic.port.PlatformButton
import com.bajobozic.port.rememberCameraManager
import org.jetbrains.compose.resources.vectorResource
import port.composeapp.generated.resources.Res
import port.composeapp.generated.resources.camera
import port.composeapp.generated.resources.visibility
import port.composeapp.generated.resources.visibility_off

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    uiState: SignInUiState,
    event: (SignInEvent) -> Unit
) {

    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    // Initialize the camera manager
    val cameraLauncher = rememberCameraManager { result ->
        imageBitmap = result
    }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    // In a real app, you would use an ActivityResultLauncher
    // for camera and image picking permissions and results.
    val onCameraButtonClick: () -> Unit = {
        cameraLauncher.capture()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // 1. User Image and Camera Button
        UserImagePicker(
            imageBitmap = imageBitmap,
            onCameraButtonClick = onCameraButtonClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Email Field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // 3. Password Field
        PasswordTextField(
            password = password,
            onPasswordChange = { password = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        PlatformButton(
            modifier = Modifier,
            label = "Create Account"
        ) {
            println("UserCreationScreen, Creating user with $email and $password")
        }
    }
}

@Composable
fun UserImagePicker(
    imageBitmap: ImageBitmap?,
    onCameraButtonClick: () -> Unit
) {
    Box(
        modifier = Modifier.size(120.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        // User Image Placeholder
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.size(120.dp)
        ) {
            if (imageBitmap != null) {
                Image(
                    bitmap = imageBitmap,
                    contentDescription = "User Profile Picture",
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "User Profile Picture Placeholder",
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
        }

        // Camera Button
        FloatingActionButton(
            onClick = onCameraButtonClick,
            modifier = Modifier.size(40.dp),
            containerColor = MaterialTheme.colorScheme.secondary
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.camera),
                contentDescription = "Take Photo",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun PasswordTextField(
    password: String,
    onPasswordChange: (String) -> Unit
) {
    var showPassword by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = { Text("Password") },
        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
        trailingIcon = {
            val image =
                if (showPassword) vectorResource(Res.drawable.visibility) else vectorResource(
                    resource = Res.drawable.visibility_off
                )
            val description = if (showPassword) "Hide password" else "Show password"

            IconButton(onClick = { showPassword = !showPassword }) {
                Icon(imageVector = image, contentDescription = description)
            }
        },
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}
package com.bajobozic.port

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.refTo
import org.jetbrains.skia.Image
import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVAuthorizationStatusNotDetermined
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.requestAccessForMediaType
import platform.Foundation.getBytes
import platform.UIKit.UIApplication
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.darwin.NSObject
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

@Composable
actual fun rememberCameraManager(onPhotoCaptured: (ImageBitmap?) -> Unit): CameraLauncher {
    val cameraDelegate = remember {
        CameraDelegate(onPhotoCaptured)
    }

    return remember {
        object : CameraLauncher {
            override fun capture() {
                val status = AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)
                when (status) {
                    AVAuthorizationStatusAuthorized -> {
                        launchCamera(cameraDelegate)
                    }

                    AVAuthorizationStatusNotDetermined -> {
                        AVCaptureDevice.requestAccessForMediaType(AVMediaTypeVideo) { granted ->
                            // callbacks from requestAccess are on a background thread, so we dispatch to main
                            dispatch_async(dispatch_get_main_queue()) {
                                if (granted) {
                                    launchCamera(cameraDelegate)
                                } else {
                                    onPhotoCaptured(null)
                                }
                            }
                        }
                    }

                    else -> {
                        // Denied or Restricted
                        onPhotoCaptured(null)
                    }
                }
            }
        }
    }
}

private fun launchCamera(delegate: CameraDelegate) {
    // 1. Check if camera is available on device (e.g. simulator has no camera)
    if (!UIImagePickerController.isSourceTypeAvailable(UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera)) {
        return
    }

    val picker = UIImagePickerController()
    picker.sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
    picker.allowsEditing = false
    picker.delegate = delegate

    // 2. Find the top-most view controller to present the camera
    // This traverses the view hierarchy to find the active controller
    val keyWindow = UIApplication.sharedApplication.keyWindow
    var topController = keyWindow?.rootViewController
    while (topController?.presentedViewController != null) {
        topController = topController.presentedViewController
    }

    topController?.presentViewController(picker, animated = true, completion = null)
}

// Delegate to handle Camera Result
private class CameraDelegate(
    private val onPhotoCaptured: (ImageBitmap?) -> Unit
) : NSObject(), UIImagePickerControllerDelegateProtocol, UINavigationControllerDelegateProtocol {

    override fun imagePickerController(
        picker: UIImagePickerController,
        didFinishPickingMediaWithInfo: Map<Any?, *>
    ) {
        val image = didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage] as? UIImage

        if (image != null) {
            val bitmap = image.toImageBitmap()
            onPhotoCaptured(bitmap)
        } else {
            onPhotoCaptured(null)
        }
        picker.dismissViewControllerAnimated(true, completion = null)
    }

    override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
        onPhotoCaptured(null)
        picker.dismissViewControllerAnimated(true, completion = null)
    }
}

// Extension to convert UIImage to Compose ImageBitmap safely
@OptIn(ExperimentalForeignApi::class)
private fun UIImage.toImageBitmap(): ImageBitmap {
    // 1. Convert UIImage to JPEG NSData (quality 1.0 = max)
    val data = UIImageJPEGRepresentation(this, 1.0) ?: return ImageBitmap(1, 1)

    // 2. Copy NSData into a Kotlin ByteArray
    val byteArray = ByteArray(data.length.toInt())
    memScoped {
        val ptr = byteArray.refTo(0).getPointer(this)
        data.getBytes(ptr, data.length)
    }

    // 3. Decode ByteArray into Skia Image -> Compose ImageBitmap
    val skiaImage = Image.makeFromEncoded(byteArray)
    return skiaImage.toComposeImageBitmap()
}

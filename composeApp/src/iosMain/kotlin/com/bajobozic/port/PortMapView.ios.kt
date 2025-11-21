package com.bajobozic.port

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.cValue
import platform.CoreLocation.CLLocationCoordinate2D
import platform.MapKit.MKMapCamera
import platform.MapKit.MKMapView

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun PortMapView() {
    UIKitView(
        factory = {
            val mapView = MKMapView()

            val nyLat = 40.7128
            val nyLong = -74.0060

            // FIX: Create the struct directly using cValue
            // instead of calling the inline function CLLocationCoordinate2DMake
            val coordinate = cValue<CLLocationCoordinate2D> {
                latitude = nyLat
                longitude = nyLong
            }

            val camera = MKMapCamera.cameraLookingAtCenterCoordinate(
                centerCoordinate = coordinate,
                fromDistance = 12000.0,
                pitch = 0.0,
                heading = 0.0
            )

            mapView.setCamera(camera, animated = true)
            mapView
        },
        modifier = Modifier.fillMaxSize(),
        update = { view ->
        }
    )
}
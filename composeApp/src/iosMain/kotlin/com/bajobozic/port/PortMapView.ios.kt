package com.bajobozic.port

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.cValue
import platform.CoreLocation.CLLocationCoordinate2D
import platform.MapKit.MKCoordinateSpan
import platform.MapKit.MKMapView

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun PortMapView() {
    UIKitView(
        factory = {
            val mapView = MKMapView()

            val nyLat = 40.7128
            val nyLong = -74.0060

            // Create the struct directly using cValue
            // instead of calling the inline function CLLocationCoordinate2DMake that can't be linked
            val coordinate = cValue<CLLocationCoordinate2D> {
                latitude = nyLat
                longitude = nyLong
            }
            val span = cValue<MKCoordinateSpan> {
                latitudeDelta = 0.05
                longitudeDelta = 0.05
            }

            val region = platform.MapKit.MKCoordinateRegionMake(coordinate, span)
            mapView.setRegion(region, animated = true)
            mapView
        },
        modifier = Modifier.fillMaxSize(),
        update = { view ->
        }
    )
}
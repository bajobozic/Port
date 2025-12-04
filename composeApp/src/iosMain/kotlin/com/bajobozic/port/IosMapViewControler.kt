package com.bajobozic.port


import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.cValue
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.useContents
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusDenied
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined
import platform.CoreLocation.kCLAuthorizationStatusRestricted
import platform.CoreLocation.kCLLocationAccuracyBest
import platform.Foundation.NSError
import platform.MapKit.MKCoordinateRegionMake
import platform.MapKit.MKCoordinateSpan
import platform.MapKit.MKMapView
import platform.UIKit.UIViewAutoresizingFlexibleHeight
import platform.UIKit.UIViewAutoresizingFlexibleWidth
import platform.UIKit.UIViewController


class IosMapViewController : UIViewController, CLLocationManagerDelegateProtocol {
    // Initialize CLLocationManager lazily to avoid confusing the constructor
    val locationManager: CLLocationManager by lazy {
        CLLocationManager()
    }

    // We need this constructor for correct UIViewController initialization
    @OptIn(BetaInteropApi::class)
    @OverrideInit
    constructor() : super(nibName = null, bundle = null)

    var currentLocation: CLLocation? = null

    // Declare the map view
    private lateinit var mapView: MKMapView

    @OptIn(ExperimentalForeignApi::class)
    override fun viewDidLoad() {
        super.viewDidLoad()

        mapView = MKMapView()
        mapView.setFrame(view.bounds) // Match the size of the controller's view
        mapView.setAutoresizingMask(UIViewAutoresizingFlexibleWidth or UIViewAutoresizingFlexibleHeight)
        view.addSubview(mapView)

        locationManager.delegate = this
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        locationManager.requestWhenInUseAuthorization()
    }

    // A helper function to easily set the map region
    @OptIn(ExperimentalForeignApi::class)
    private fun setupMapRegion(latitude: Double, longitude: Double) {
        //must use useContents to access iOS struct members , other options is to use memScoped for accessing struct members
        memScoped {
            // Create the central coordinate
            val coordinate = CLLocationCoordinate2DMake(latitude, longitude)

            // Define the zoom level (span)
            val span = cValue<MKCoordinateSpan> {
                this.latitudeDelta = 0.05
                this.longitudeDelta = 0.05
            }

            // Create the region and apply it
            val region = MKCoordinateRegionMake(coordinate, span)
            mapView.setRegion(region, animated = true)
        }
    }

    // MARK: - CLLocationManagerDelegate

    override fun locationManagerDidChangeAuthorization(manager: CLLocationManager) {
        when (manager.authorizationStatus()) {
            kCLAuthorizationStatusAuthorizedWhenInUse,
            kCLAuthorizationStatusAuthorizedAlways -> {
                mapView.showsUserLocation = true
                locationManager.startUpdatingLocation() // Start updates after authorization
            }

            kCLAuthorizationStatusDenied,
            kCLAuthorizationStatusRestricted -> {
                // Handle denied or restricted access (e.g., show an alert)
                println("Location access denied or restricted.")
            }

            kCLAuthorizationStatusNotDetermined -> {
                // Authorization not yet determined, request again if needed
                println("Location authorization not determined.")
            }

            else -> {
                println("Unknown authorization status")
            }
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun locationManager(
        manager: CLLocationManager,
        didUpdateLocations: List<*>
    ) {
        val location = didUpdateLocations.last() as? CLLocation
        location?.let {
            currentLocation = it
        }
        //must use useContents to access iOS struct members , other options is to use memScoped for accessing struct members
        val (latitude, longitude) = location?.coordinate?.useContents {
            latitude to longitude
        } ?: (0.0 to 0.0)
        setupMapRegion(latitude, longitude)
        println("Current location: $latitude, $longitude")
    }

    // 🚨 ADD 'override' keyword here
    override fun locationManager(manager: CLLocationManager, didFailWithError: NSError) {
        println("Location manager failed with error: $(didFailWithError.localizedDescription)")
    }
}

//
//  AppDelegate.swift
//  iosApp
//
//  Created by Savo Bozic on 1. 12. 2025..
//


import SwiftUI
import ComposeApp
import FirebaseCore
import FirebaseMessaging

class AppDelegate: NSObject, UIApplicationDelegate {

    func application(_ application: UIApplication,
                     didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil) -> Bool {

        FirebaseApp.configure() //important

        //By default showPushNotification value is true.
        //When set showPushNotification to false foreground push  notification will not be shown.
        //You can still get notification content using #onPushNotification listener method.
        NotifierManager.shared.initialize(configuration: NotificationPlatformConfigurationIos(
            showPushNotification: true,
            askNotificationPermissionOnStart: true,
            notificationSoundName: nil
        )
        )

        return true
    }

    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        Messaging.messaging().apnsToken = deviceToken
    }

}



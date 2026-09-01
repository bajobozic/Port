# KMPNotifier Migration Plan

Migrate the codebase from the deprecated `NotifierManager` API to the new `KMPNotifier` API with pluggable extensions (`LocalNotifications` and `FirebasePush`), resolving all build warnings and deprecations.

## User Review Required

> [!NOTE]
> The migration will update KMPNotifier initialization and listener registration across all target platforms: Android, iOS, and Desktop.

No breaking architectural changes are introduced; we are mapping deprecated 1.x-style singleton calls to the new 2.x-style pluggable architecture recommended in the library's `MIGRATION.md`.

## Proposed Changes

### 1. Shared / Common Module

#### [MODIFY] [OnApplicationStart.kt](file:///Users/savo/Developer/Port/shared/src/commonMain/kotlin/com/bajobozic/port/OnApplicationStart.kt)
- Replace `NotifierManager` extension with `KMPNotifier` extension.
- Use `KMPNotifier.initialize` with `FirebasePush` pluggable extension.
- Split the previous unified `NotifierManager.Listener` into `KMPNotifier.Listener` (for click events) and `PushListener` (for Firebase push token and payload events).

#### [MODIFY] [OnApplicationStart.ios.kt](file:///Users/savo/Developer/Port/shared/src/iosMain/kotlin/com/bajobozic/port/OnApplicationStart.ios.kt)
- Use `KMPNotifier` instead of `NotifierManager`.

#### [MODIFY] [OnApplicationStart.android.kt](file:///Users/savo/Developer/Port/shared/src/androidMain/kotlin/com/bajobozic/port/OnApplicationStart.android.kt)
- Use `KMPNotifier` instead of `NotifierManager`.

---

### 2. Desktop Module

#### [MODIFY] [Main.kt](file:///Users/savo/Developer/Port/desktopApp/src/jvmMain/kotlin/com/bajobozic/port/Main.kt)
- Replace `NotifierManager.initialize` with `KMPNotifier.initialize(configuration, LocalNotifications)`.

---

### 3. Detail UI Module

#### [MODIFY] [DetailsScreen.kt](file:///Users/savo/Developer/Port/detail_ui/src/commonMain/kotlin/com/bajobozic/detail_ui/presentation/DetailsScreen.kt)
- Replace `NotifierManager.getLocalNotifier()` with `LocalNotifications.notifier` to obtain local notification capabilities.

---

### 4. Android App Module

#### [MODIFY] [MainActivity.kt](file:///Users/savo/Developer/Port/androidApp/src/androidMain/kotlin/com/bajobozic/port/MainActivity.kt)
- Replace `NotifierManager.onCreateOrOnNewIntent(intent)` with `KMPNotifier.onCreateOrOnNewIntent(intent)`.

---

### 5. iOS App Module

#### [MODIFY] [AppDelegate.swift](file:///Users/savo/Developer/Port/iosApp/iosApp/AppDelegate.swift)
- Replace `NotifierManager.shared.onApplicationDidReceiveRemoteNotification(userInfo: userInfo)` with `KMPNotifier.shared.onApplicationDidReceiveRemoteNotification(userInfo: userInfo)`.

## Verification Plan

### Automated Tests & Builds
- Run `./gradlew :shared:compileKotlinAnd` or run compile/build on all platforms to ensure there are no deprecation warnings or build errors related to KMPNotifier.
- Execute a gradle build.

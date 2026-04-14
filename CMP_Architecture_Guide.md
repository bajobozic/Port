# Structuring 3rd-Party Libraries in Compose Multiplatform (No-CocoaPods Architecture)

When building a rich media application like a Compose Multiplatform app (e.g., Port), handling
3rd-party libraries requires a clean separation of concerns. This guide outlines how to integrate
platform-specific SDKs across Android, iOS, and JVM, keeping Clean Architecture and MVI principles
intact while strictly using Swift Package Manager (SPM) for iOS.

## 1. UI Libraries (e.g., Maps, Video Players, Ad Views)

UI libraries require bridging native rendering engines (`Views`, `UIViews`, or `JComponents`) into
the Compose tree. You have to manage native view lifecycles, touch events, and state
synchronization.

### The Implementation Strategy:

* **Common Source Set (`commonMain`):**
  Define an `expect @Composable` function. Create a platform-agnostic state holder (e.g.,
  `GMapsState`) and a Kotlin interface (e.g., `IGMapsBridge`) that will eventually provide the
  native view. Your common domain code remains completely ignorant of native SDKs.
* **Android (`androidMain`):**
  Import the library via standard Gradle dependencies. Use the `actual @Composable` function to wrap
  the native Android view using `AndroidView`, passing the native instance (or use the library
  directly if a Jetpack Compose wrapper already exists).
* **iOS (`iosMain` & Swift via SPM):**
  Because Kotlin Native cannot directly import SPM modules into `iosMain`, you must use a bridging
  strategy:
    1. **Add Dependency:** Add the library (e.g., Google Maps) to your iOS project using Xcode's
       native **Swift Package Manager (SPM)**.
    2. **Swift Implementation:** Write a Swift class that implements your Kotlin interface (
       `IGMapsBridge`), instantiates the native `UIView` (e.g., `GMSMapView`), and sets up native
       delegates for touch events.
    3. **Dependency Injection:** Use a DI framework (like Koin) to inject this Swift implementation
       back into your Kotlin `iosMain` code at app launch.
    4. **Compose Wrapper:** Wrap the injected Swift view using Compose Multiplatform's `UIKitView`.
* **JVM (`jvmMain` / Desktop):**
  Import the library via Gradle (e.g., a Java Swing or JavaFX component). Use `SwingPanel` to embed
  the `JComponent` directly into the Compose desktop tree.

### State and Interaction (The MVI approach):

Native views don't understand Compose state. You create a unified state class in common code. In
your native implementations, you observe this state (e.g., launching a coroutine to collect a
`SharedFlow` of camera updates) and imperatively update the native view. Conversely, native delegate
callbacks (like map taps handled in Swift) trigger common Kotlin lambdas to pass events back up to
your ViewModel.

---

## 2. Functional Libraries (e.g., Analytics, Auth, Firebase, Recaptcha)

Functional libraries do not render anything on the screen. They perform background tasks, network
calls, or data processing. Without CocoaPods, you cannot rely on automatic `cinterop` generation for
iOS dependencies, so you will utilize the same clean bridging pattern used for UI, just without the
View wrappers.

### The Implementation Strategy:

* **Prioritize Pure KMP Libraries:** Always look for KMP-native libraries first (e.g., Ktor for
  networking, Multiplatform Settings for key-value storage, Firebase Kotlin SDK). These require zero
  platform-specific bridging and are handled entirely in `commonMain` via Gradle.
* **When using Native-Only SDKs (The Bridge Pattern):**
  If a library only provides distinct Android, iOS, and Java SDKs (like a specific Recaptcha SDK or
  custom Analytics):
    1. **Common Code:** Define a standard Kotlin `interface` (e.g., `IAuthService`) detailing the
       required suspend functions or data flows.
    2. **Android (`androidMain`):** Add the library via Gradle. Write an implementation of the
       interface that calls the Android SDK directly.
    3. **iOS (Swift + SPM):**
        * Add the functional SDK to your Xcode project via **SPM**.
        * Write a Swift class that implements the `IAuthService` Kotlin interface.
        * Inside the Swift class, call the SPM library methods.
        * Inject this Swift class into your Kotlin code using Koin during the iOS app
          initialization.
    4. **JVM (`jvmMain`):** Add the Java library via Gradle and write the interface implementation.

### How the implementation differs from UI:

1. **No UI Wrappers:** You do not use `UIKitView`, `AndroidView`, or `SwingPanel`.
2. **Data Flow vs. State Sync:** Instead of manually synchronizing UI state, you are simply
   executing asynchronous calls. An `authenticate(token: String)` interface function triggers
   standard Swift/Kotlin bridging to return data or throw exceptions, perfectly slotting into your
   Repository or UseCase layers.

---

## Summary Comparison

| Characteristic              | UI Libraries (Maps, Video Players)                                                     | Functional Libraries (Auth, Analytics)                     |
|:----------------------------|:---------------------------------------------------------------------------------------|:-----------------------------------------------------------|
| **Compose Integration**     | Wrapped in `AndroidView`, `UIKitView`, or `SwingPanel`                                 | Completely decoupled from Compose UI                       |
| **Common Code Abstraction** | `expect @Composable` and an Interface returning a Native View                          | Standard Kotlin `interface` returning data/Flows           |
| **iOS Dependency Manager**  | **Strictly Swift Package Manager (SPM)**                                               | **Strictly Swift Package Manager (SPM)**                   |
| **iOS Bridging Method**     | Swift class implements Kotlin Interface -> Injected via DI                             | Swift class implements Kotlin Interface -> Injected via DI |
| **State Management**        | Requires manual synchronization between Compose State and imperative native properties | Standard `suspend` functions or Kotlin Flows               |
| **Complexity**              | High (Lifecycle, touch events, clipping limits)                                        | Low to Medium (Standard asynchronous data passing)         |

By enforcing the Interface + Swift implementation pattern for **all** iOS dependencies via SPM, your
architecture remains highly resilient to platform changes, keeps domain logic pristine, and
completely insulates you from the impending CocoaPods deprecation.

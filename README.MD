# Weather APP using Compose Multiplatform

## creation
- current app is created using [Compose-Multiplatform-Wizard](https://terrakok.github.io/Compose-Multiplatform-Wizard/)
- it should be same as [official compose Multiplatform Wizard](https://kmp.jetbrains.com/?_gl=1*1f010cw*_gcl_au*MTQ2Mjg4NDQ1Ni4xNzM5MDI3ODc4*_ga*MTc3MzYzOTU3MS4xNzM5MDI3ODQ3*_ga_9J976DJZ68*MTc0NTI0MzYyMi40LjEuMTc0NTI0MzgzNC41MS4wLjA.#newProject)
- but it has more features by providing useful libraries and plugins that you may need while develop APP.
- for current APP we just select IOS and android and for the main libraries and plugins you can find all of them in [libs.versions.toml](gradle/libs.versions.toml)

### Note: didn't know if this there is any risk to start real project with this non official library but because this just test project for learn it is ok to use it.

## current app contains following feature
- get current user location in first launch of the app

- search for weather by location or by city name

- show Air quality 

- more details about location

- handle error

### Note: current app just has two layer (UI and data) and domain layer because the business and feature is simple and it just has 3 screens and single API call.

# Compose Multiplatform Application configurations

## Before running!
- check your system with [KDoctor](https://github.com/Kotlin/kdoctor)
- install JDK 17 or higher on your machine (JDK 21 recommended)
- add `local.properties` file to the project root and set a path to Android SDK there

### Android
To run the application on android device/emulator:
- open project in Android Studio and run imported android run configuration

To build the application bundle:
- run `./gradlew :composeApp:assembleDebug`
- find `.apk` file in `composeApp/build/outputs/apk/debug/composeApp-debug.apk`
  Run android UI tests on the connected device: `./gradlew :composeApp:connectedDebugAndroidTest`

### iOS
To run the application on iPhone device/simulator:
- Open `iosApp/iosApp.xcproject` in Xcode and run standard configuration
- Or use [Kotlin Multiplatform Mobile plugin](https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform-mobile) for Android Studio
  Run iOS simulator UI tests: `./gradlew :composeApp:iosSimulatorArm64Test`


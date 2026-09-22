# android-lessons — QR App

Android app to **scan** and **generate** QR codes, built with Kotlin and Jetpack Compose.

## Features

- **Scan**: reads QR codes with the camera (CameraX + ML Kit). Copy the result, or open it if it is a link.
- **Generate**: type text or a URL and the QR code is drawn as you type (ZXing).

## Stack

| What | Library |
| --- | --- |
| UI | Jetpack Compose + Material 3 |
| Architecture | MVVM with Jetpack ViewModels + `StateFlow` |
| Dependency injection | Hilt (KSP) |
| Camera | CameraX |
| QR reading | ML Kit Barcode Scanning |
| QR generation | ZXing core |

`minSdk` 26 · `targetSdk` 35 · JDK 17

## Project structure

```
app/src/main/java/com/jcjiron/qrapp/
├── QrApplication.kt              # @HiltAndroidApp
├── data/
│   ├── qr/ZxingQrEncoder.kt      # On-device data source: text → QR modules
│   └── repository/QrRepositoryImpl.kt
├── domain/                       # Pure Kotlin, no Android imports
│   ├── model/                    # QrCode, ScannedCode, GenerateQrResult
│   └── repository/QrRepository.kt
├── presentation/
│   ├── theme/                    # Color.kt, Type.kt, Shape.kt, Theme.kt
│   ├── main/                     # MainActivity, MainViewModel, MainScreen (bottom tabs)
│   ├── scan/                     # ScanScreen, ScanViewModel, QrCodeAnalyzer (CameraX → ML Kit)
│   └── generate/                 # GenerateScreen, GenerateViewModel, QrCodeImage
└── di/                           # Hilt modules: RepositoryModule, ScannerModule
```

## Android rules

How this project is built. One rule, one line.

### Architecture

- Clean Architecture in a single module, packaged by layer: `data`, `domain`, `presentation`, `di`.
- Dependencies point inward: `presentation` → `domain` ← `data`; `domain` knows nothing about Android.
- MVVM with Jetpack ViewModels; no MVP and no hand-updated listeners.
- The ViewModel exposes state as `StateFlow` and the UI only observes it.

### Data

- A `Repository` orchestrates the data sources; the ViewModel never knows where the data comes from.
- Remote and local data sources are always separate classes.
- When the app needs them: Retrofit is the default HTTP client (`data/remote`, `RemoteDataSource`) and Room is the default local database (`data/local`, `LocalDataSource`).

### Dependency injection

- Hilt from the first commit; no dependency is instantiated by hand.
- Hilt modules live in the `di` package, one per responsibility (e.g. repositories, scanner, network, database).

### UI and theme

- Jetpack Compose for all UI; no XML layouts.
- `LightColors` and `DarkColors` live in separate variables so the theme can be switched in one line.
- The theme defines colors, typography, shapes and surfaces; no composable hardcodes styles.

### Build

- Gradle with Kotlin DSL (`.kts`) and dependencies in the version catalog (`gradle/libs.versions.toml`).
- Two build types: `debug` with `applicationIdSuffix = ".debug"` and `release` with no suffix.
- When the app talks to a server, each build type injects its server URL via `buildConfigField` (with `buildConfig = true`).
- Product flavors are declared from the start, even if commented out, because sooner or later they are needed.

### Not used yet in this app

The QR app works fully offline and stores nothing, so it has no `data/remote`, `data/local`, Retrofit, Room or server URL `buildConfigField` yet. They get added, following the rules above, once a feature needs them (e.g. a scan history → Room).

## Running it

1. Open the project in Android Studio.
2. Run the `app` configuration on a phone or emulator (the emulator can use your webcam as the back camera).

The debug build installs as `com.jcjiron.qrapp.debug`, so it can sit next to a release build on the same phone.

From the terminal:

```bash
./gradlew testDebugUnitTest   # unit tests (domain + data)
./gradlew installDebug        # install on the connected device
```

Every push runs the tests and builds a debug APK in GitHub Actions; download it from the run's **Artifacts** section (`qr-app-debug`) to install on a phone.

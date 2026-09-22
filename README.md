# android-lessons — Mi QR

Single-screen Android app to carry your work QR code on your phone instead of a printed badge.

## How it works

- **First time**: the screen shows a **Cargar foto** button. It opens the gallery (system photo picker, no permissions needed); pick the QR image you received on Teams.
- The app keeps its own copy of the image, so it is still there next time you open the app, even if you delete the original from the gallery.
- **While the QR is showing**, the screen goes to **full brightness** and stays on so the desk scanner can read it. Your normal brightness comes back as soon as you leave the app.
- The **⋮** menu in the toolbar has **Borrar imagen**, which removes the saved image so you can load a different one.

## Stack

| What | Library |
| --- | --- |
| UI | Jetpack Compose + Material 3 |
| Architecture | MVVM with Jetpack ViewModels + `StateFlow` |
| Dependency injection | Hilt (KSP) |
| Image loading | Coil |

`minSdk` 26 · `targetSdk` 35 · JDK 17

## Project structure

```
app/src/main/java/com/jcjiron/qrapp/
├── QrApplication.kt                       # @HiltAndroidApp
├── data/
│   ├── local/QrImageLocalDataSource.kt    # Copies the picked image into internal storage
│   └── repository/QrImageRepositoryImpl.kt
├── domain/                                # Pure Kotlin, no Android imports
│   ├── model/QrImage.kt
│   └── repository/QrImageRepository.kt
├── presentation/
│   ├── theme/                             # Color.kt, Type.kt, Shape.kt, Theme.kt
│   └── main/                              # MainActivity, MainViewModel, MainUiState,
│                                          # MainScreen, MaxBrightnessEffect
└── di/                                    # Hilt modules: RepositoryModule, DispatcherModule
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
- Hilt modules live in the `di` package, one per responsibility (e.g. repositories, dispatchers, network, database).

### UI and theme

- Jetpack Compose for all UI; no XML layouts.
- `LightColors` and `DarkColors` live in separate variables so the theme can be switched in one line.
- The theme defines colors, typography, shapes and surfaces; no composable hardcodes styles.

### Build

- Gradle with Kotlin DSL (`.kts`) and dependencies in the version catalog (`gradle/libs.versions.toml`).
- Two build types: `debug` with `applicationIdSuffix = ".debug"` and `release` with no suffix.
- When the app talks to a server, each build type injects its server URL via `buildConfigField` (with `buildConfig = true`).
- Product flavors are declared from the start, even if commented out, because sooner or later they are needed.

### How the rules apply here

- The only data is one image file, so `QrImageLocalDataSource` stores it as a file in internal storage instead of a Room database. Room comes in once there is structured data to keep (e.g. several badges).
- The app works offline, so there is no `data/remote`, Retrofit or server URL `buildConfigField` yet. They get added following the rules above once a feature needs a server.

## Running it

1. Open the project in Android Studio.
2. Run the `app` configuration on a phone or emulator.

The debug build installs as `com.jcjiron.qrapp.debug`, so it can sit next to a release build on the same phone.

From the terminal:

```bash
./gradlew testDebugUnitTest   # unit tests
./gradlew installDebug        # install on the connected device
```

Every push runs the tests and builds a debug APK in GitHub Actions; download it from the run's **Artifacts** section (`qr-app-debug`) to install on a phone.

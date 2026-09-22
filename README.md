# android-lessons — QR App

App de Android para **leer** y **generar** códigos QR, hecha con Kotlin y Jetpack Compose.

## Funciones

- **Escanear**: usa la cámara (CameraX) y ML Kit para leer QR. Puedes copiar el resultado o abrirlo si es un link.
- **Generar**: escribe un texto o URL y se genera el QR al momento (ZXing).

## Stack

| Qué | Librería |
| --- | --- |
| UI | Jetpack Compose + Material 3 |
| Cámara | CameraX (`camera-camera2`, `camera-lifecycle`, `camera-view`) |
| Lectura de QR | ML Kit Barcode Scanning |
| Generación de QR | ZXing core |

`minSdk` 26 · `targetSdk` 35 · JDK 17

## Estructura

```
app/src/main/java/com/jcjiron/qrapp/
├── MainActivity.kt          # Scaffold con barra inferior (Escanear / Generar)
├── qr/
│   ├── QrAnalyzer.kt        # Frames de CameraX → ML Kit
│   ├── QrEncoder.kt         # Texto → BitMatrix (ZXing)
│   └── BitMatrixExt.kt      # BitMatrix → Bitmap
└── ui/
    ├── scan/ScanScreen.kt   # Permiso de cámara, preview y resultado
    ├── generate/GenerateScreen.kt
    └── theme/Theme.kt
```

## Cómo correrla

1. Abre el proyecto en Android Studio.
2. Corre la configuración `app` en un teléfono o emulador (el emulador puede usar la webcam como cámara trasera).

Desde la terminal:

```bash
./gradlew testDebugUnitTest   # tests del generador de QR
./gradlew installDebug        # instala en el dispositivo conectado
```

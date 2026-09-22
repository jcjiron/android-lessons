package com.jcjiron.qrapp.presentation.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

private val Navy = Color(0xFF1E3A8A)
private val Teal = Color(0xFF0E7490)
private val SkyBlue = Color(0xFF93C5FD)
private val Cyan = Color(0xFF67E8F9)

val LightColors = lightColorScheme(
    primary = Navy,
    secondary = Teal,
)

val DarkColors = darkColorScheme(
    primary = SkyBlue,
    secondary = Cyan,
)

// QR codes stay black on white in both themes: scanners read high contrast best.
val QrDarkModule = Color.Black
val QrLightModule = Color.White

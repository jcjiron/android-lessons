package com.jcjiron.qrapp.qr

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.common.BitMatrix

/** Pinta la matriz del QR en un [Bitmap]: negro para módulos activos, blanco para el fondo. */
fun BitMatrix.toBitmap(): Bitmap {
    val pixels = IntArray(width * height) { i ->
        if (get(i % width, i / width)) Color.BLACK else Color.WHITE
    }
    return Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888)
}

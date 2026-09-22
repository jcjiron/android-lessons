package com.jcjiron.qrapp.qr

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

/**
 * Convierte texto en la matriz de módulos (cuadritos) de un código QR usando ZXing.
 *
 * No depende de Android, así que se puede probar con tests unitarios normales.
 */
object QrEncoder {

    private val hints = mapOf(
        EncodeHintType.CHARACTER_SET to "UTF-8",
        EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.M,
        EncodeHintType.MARGIN to 1,
    )

    /**
     * @param size ancho/alto deseado en píxeles.
     * @throws IllegalArgumentException si [text] está vacío.
     * @throws WriterException si el texto no cabe en un QR.
     */
    fun encode(text: String, size: Int): BitMatrix {
        require(text.isNotEmpty()) { "El texto no puede estar vacío" }
        return QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, size, size, hints)
    }
}

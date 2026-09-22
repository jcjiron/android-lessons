package com.jcjiron.qrapp.data.qr

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.jcjiron.qrapp.domain.model.QrCode
import javax.inject.Inject

/** Encodes text into a [QrCode] with ZXing. Runs fully on-device. */
class ZxingQrEncoder @Inject constructor() {

    private val hints = mapOf(
        EncodeHintType.CHARACTER_SET to "UTF-8",
        EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.M,
        EncodeHintType.MARGIN to 1,
    )

    /**
     * @throws IllegalArgumentException if [content] is empty.
     * @throws WriterException if [content] does not fit in a QR code.
     */
    fun encode(content: String): QrCode {
        // A 0x0 target makes ZXing return exactly one pixel per module.
        val matrix = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, 0, 0, hints)
        val size = matrix.width
        return QrCode(size, BooleanArray(size * size) { i -> matrix.get(i % size, i / size) })
    }
}

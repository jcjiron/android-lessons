package com.jcjiron.qrapp.data.qr

import com.google.zxing.BinaryBitmap
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.WriterException
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeReader
import com.jcjiron.qrapp.domain.model.QrCode
import org.junit.Assert.assertEquals
import org.junit.Test

class ZxingQrEncoderTest {

    private val encoder = ZxingQrEncoder()

    @Test
    fun `encoded QR decodes back to the same text`() {
        val text = "https://github.com/jcjiron/android-lessons"
        assertEquals(text, decode(encoder.encode(text)))
    }

    @Test
    fun `supports accents and emoji`() {
        val text = "¡Hola, año nuevo! 🎉"
        assertEquals(text, decode(encoder.encode(text)))
    }

    @Test
    fun `short text gives a version 1 QR with a one-module margin`() {
        // Version 1 = 21x21 modules, plus 1 module of quiet zone on each side.
        assertEquals(23, encoder.encode("hola").size)
    }

    @Test(expected = WriterException::class)
    fun `text that does not fit throws`() {
        encoder.encode("a".repeat(5000))
    }

    /** Scales the QR up and reads it back with ZXing to prove it is valid. */
    private fun decode(qrCode: QrCode, scale: Int = 8): String {
        val side = qrCode.size * scale
        val pixels = IntArray(side * side) { i ->
            if (qrCode[(i % side) / scale, (i / side) / scale]) 0xFF000000.toInt() else 0xFFFFFFFF.toInt()
        }
        val source = RGBLuminanceSource(side, side, pixels)
        return QRCodeReader().decode(BinaryBitmap(HybridBinarizer(source))).text
    }
}

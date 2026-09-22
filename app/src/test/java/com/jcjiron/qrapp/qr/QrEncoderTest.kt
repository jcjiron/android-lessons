package com.jcjiron.qrapp.qr

import com.google.zxing.BinaryBitmap
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeReader
import org.junit.Assert.assertEquals
import org.junit.Test

class QrEncoderTest {

    @Test
    fun `el QR generado se puede leer de vuelta`() {
        val text = "https://github.com/jcjiron/android-lessons"
        assertEquals(text, decode(QrEncoder.encode(text, 256)))
    }

    @Test
    fun `soporta acentos y emojis`() {
        val text = "¡Hola, año nuevo! 🎉"
        assertEquals(text, decode(QrEncoder.encode(text, 256)))
    }

    @Test
    fun `respeta el tamaño pedido`() {
        val matrix = QrEncoder.encode("hola", 300)
        assertEquals(300, matrix.width)
        assertEquals(300, matrix.height)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `texto vacío lanza error`() {
        QrEncoder.encode("", 256)
    }

    @Test(expected = WriterException::class)
    fun `texto demasiado largo lanza error`() {
        QrEncoder.encode("a".repeat(5000), 256)
    }

    /** Lee el QR con ZXing, sin Android, para comprobar que es válido. */
    private fun decode(matrix: BitMatrix): String {
        val pixels = IntArray(matrix.width * matrix.height) { i ->
            if (matrix.get(i % matrix.width, i / matrix.width)) 0xFF000000.toInt() else 0xFFFFFFFF.toInt()
        }
        val source = RGBLuminanceSource(matrix.width, matrix.height, pixels)
        return QRCodeReader().decode(BinaryBitmap(HybridBinarizer(source))).text
    }
}

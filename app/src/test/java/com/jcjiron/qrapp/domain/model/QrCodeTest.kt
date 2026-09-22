package com.jcjiron.qrapp.domain.model

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class QrCodeTest {

    @Test
    fun `get reads modules row by row`() {
        // Row 0: dark, light / Row 1: light, dark
        val qr = QrCode(2, booleanArrayOf(true, false, false, true))
        assertTrue(qr[0, 0])
        assertFalse(qr[1, 0])
        assertFalse(qr[0, 1])
        assertTrue(qr[1, 1])
    }

    @Test(expected = IllegalArgumentException::class)
    fun `rejects a module count that is not size squared`() {
        QrCode(3, BooleanArray(4))
    }
}

package com.jcjiron.qrapp.domain.model

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ScannedCodeTest {

    @Test
    fun `http and https are links, case insensitive`() {
        assertTrue(ScannedCode("https://example.com").isLink)
        assertTrue(ScannedCode("HTTP://example.com").isLink)
    }

    @Test
    fun `plain text and other schemes are not links`() {
        assertFalse(ScannedCode("hola mundo").isLink)
        assertFalse(ScannedCode("WIFI:S:casa;T:WPA;P:1234;;").isLink)
        assertFalse(ScannedCode("mailto:a@b.com").isLink)
    }
}

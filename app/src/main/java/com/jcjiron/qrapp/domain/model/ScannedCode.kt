package com.jcjiron.qrapp.domain.model

/** Content read from a QR code. */
data class ScannedCode(val content: String) {
    val isLink: Boolean
        get() = content.startsWith("http://", ignoreCase = true) ||
            content.startsWith("https://", ignoreCase = true)
}

package com.jcjiron.qrapp.domain.model

/**
 * A QR code as a square grid of modules (the little squares), quiet zone included.
 *
 * It is resolution-independent: the UI decides how many pixels each module takes.
 */
class QrCode(val size: Int, private val modules: BooleanArray) {

    init {
        require(modules.size == size * size) { "Expected ${size * size} modules, got ${modules.size}" }
    }

    /** `true` when the module at column [x], row [y] is dark. */
    operator fun get(x: Int, y: Int): Boolean = modules[y * size + x]
}

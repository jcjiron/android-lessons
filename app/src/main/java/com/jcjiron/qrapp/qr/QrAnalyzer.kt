package com.jcjiron.qrapp.qr

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.io.Closeable

/**
 * Recibe cada frame de CameraX y se lo pasa a ML Kit para buscar códigos QR.
 *
 * [onQrDetected] se llama en el hilo principal con el contenido del primer QR encontrado.
 */
class QrAnalyzer(
    private val onQrDetected: (String) -> Unit,
) : ImageAnalysis.Analyzer, Closeable {

    private val scanner = BarcodeScanning.getClient(
        BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
    )

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage == null) {
            imageProxy.close()
            return
        }

        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                barcodes.firstNotNullOfOrNull { it.rawValue }?.let(onQrDetected)
            }
            // Hay que cerrar el frame siempre; si no, CameraX deja de mandar frames nuevos.
            .addOnCompleteListener { imageProxy.close() }
    }

    override fun close() {
        scanner.close()
    }
}

package com.jcjiron.qrapp.presentation.generate

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import com.jcjiron.qrapp.domain.model.QrCode
import com.jcjiron.qrapp.presentation.theme.QrDarkModule
import com.jcjiron.qrapp.presentation.theme.QrLightModule

/** Draws a [QrCode] scaled to fill [modifier]'s size, with crisp module edges. */
@Composable
fun QrCodeImage(qrCode: QrCode, contentDescription: String?, modifier: Modifier = Modifier) {
    val bitmap = remember(qrCode) { qrCode.toImageBitmap() }
    Image(
        bitmap = bitmap,
        contentDescription = contentDescription,
        // One pixel per module: no smoothing, so scaling up keeps sharp squares.
        filterQuality = FilterQuality.None,
        modifier = modifier,
    )
}

private fun QrCode.toImageBitmap(): ImageBitmap {
    val dark = QrDarkModule.toArgb()
    val light = QrLightModule.toArgb()
    val pixels = IntArray(size * size) { i -> if (this[i % size, i / size]) dark else light }
    return Bitmap.createBitmap(pixels, size, size, Bitmap.Config.ARGB_8888).asImageBitmap()
}

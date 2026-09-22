package com.jcjiron.qrapp.ui.generate

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jcjiron.qrapp.R
import com.jcjiron.qrapp.qr.QrEncoder
import com.jcjiron.qrapp.qr.toBitmap

private const val QR_SIZE_PX = 512

@Composable
fun GenerateScreen(modifier: Modifier = Modifier) {
    var text by rememberSaveable { mutableStateOf("") }

    // Se vuelve a calcular solo cuando cambia el texto.
    val qrImage = remember(text) {
        if (text.isEmpty()) {
            null
        } else {
            // encode() falla si el texto es demasiado largo para un QR.
            runCatching { QrEncoder.encode(text, QR_SIZE_PX).toBitmap().asImageBitmap() }.getOrNull()
        }
    }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text(stringResource(R.string.generate_label)) },
            placeholder = { Text(stringResource(R.string.generate_placeholder)) },
            modifier = Modifier.fillMaxWidth(),
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            contentAlignment = Alignment.Center,
        ) {
            if (qrImage != null) {
                Image(
                    bitmap = qrImage,
                    contentDescription = stringResource(R.string.qr_content_description),
                    // Sin suavizado para que los bordes de los módulos queden nítidos.
                    filterQuality = FilterQuality.None,
                    modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                )
            } else {
                Text(
                    text = stringResource(
                        if (text.isEmpty()) R.string.generate_empty else R.string.generate_too_long
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

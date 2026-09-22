package com.jcjiron.qrapp.presentation.generate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jcjiron.qrapp.R

@Composable
fun GenerateScreen(
    modifier: Modifier = Modifier,
    viewModel: GenerateViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        OutlinedTextField(
            value = uiState.text,
            onValueChange = viewModel::onTextChange,
            label = { Text(stringResource(R.string.generate_label)) },
            placeholder = { Text(stringResource(R.string.generate_placeholder)) },
            isError = uiState.isTooLong,
            modifier = Modifier.fillMaxWidth(),
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            contentAlignment = Alignment.Center,
        ) {
            val qrCode = uiState.qrCode
            if (qrCode != null) {
                QrCodeImage(
                    qrCode = qrCode,
                    contentDescription = stringResource(R.string.qr_content_description),
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                Text(
                    text = stringResource(
                        if (uiState.isTooLong) R.string.generate_too_long else R.string.generate_empty
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (uiState.isTooLong) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                )
            }
        }
    }
}

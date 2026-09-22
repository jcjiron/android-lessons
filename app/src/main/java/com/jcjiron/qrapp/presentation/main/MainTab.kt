package com.jcjiron.qrapp.presentation.main

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.ui.graphics.vector.ImageVector
import com.jcjiron.qrapp.R

enum class MainTab(@StringRes val label: Int, val icon: ImageVector) {
    Scan(R.string.tab_scan, Icons.Filled.QrCodeScanner),
    Generate(R.string.tab_generate, Icons.Filled.QrCode),
}

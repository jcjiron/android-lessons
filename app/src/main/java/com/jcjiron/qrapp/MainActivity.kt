package com.jcjiron.qrapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.jcjiron.qrapp.ui.generate.GenerateScreen
import com.jcjiron.qrapp.ui.scan.ScanScreen
import com.jcjiron.qrapp.ui.theme.QrAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QrAppTheme {
                QrApp()
            }
        }
    }
}

private enum class Tab(@StringRes val label: Int, val icon: ImageVector) {
    Scan(R.string.tab_scan, Icons.Filled.QrCodeScanner),
    Generate(R.string.tab_generate, Icons.Filled.QrCode),
}

@Composable
private fun QrApp() {
    var selectedTab by rememberSaveable { mutableStateOf(Tab.Scan) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                Tab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = tab == selectedTab,
                        onClick = { selectedTab = tab },
                        icon = { Icon(tab.icon, contentDescription = null) },
                        label = { Text(stringResource(tab.label)) },
                    )
                }
            }
        },
    ) { innerPadding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        when (selectedTab) {
            Tab.Scan -> ScanScreen(contentModifier)
            Tab.Generate -> GenerateScreen(contentModifier)
        }
    }
}

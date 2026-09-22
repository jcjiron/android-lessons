package com.jcjiron.qrapp.presentation.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jcjiron.qrapp.domain.model.ScannedCode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ScanUiState {
    data object Scanning : ScanUiState
    data class Scanned(val code: ScannedCode) : ScanUiState
}

@HiltViewModel
class ScanViewModel @Inject constructor(
    val analyzer: QrCodeAnalyzer,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ScanUiState>(ScanUiState.Scanning)
    val uiState: StateFlow<ScanUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            analyzer.detections.collect { content ->
                // Keep the first result; ML Kit may still report codes while the camera closes.
                if (_uiState.value == ScanUiState.Scanning) {
                    _uiState.value = ScanUiState.Scanned(ScannedCode(content))
                }
            }
        }
    }

    fun onScanAgain() {
        _uiState.value = ScanUiState.Scanning
    }
}

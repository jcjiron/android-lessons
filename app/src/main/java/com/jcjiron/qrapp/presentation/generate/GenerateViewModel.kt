package com.jcjiron.qrapp.presentation.generate

import androidx.lifecycle.ViewModel
import com.jcjiron.qrapp.domain.model.GenerateQrResult
import com.jcjiron.qrapp.domain.model.QrCode
import com.jcjiron.qrapp.domain.repository.QrRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class GenerateUiState(
    val text: String = "",
    val qrCode: QrCode? = null,
    val isTooLong: Boolean = false,
)

@HiltViewModel
class GenerateViewModel @Inject constructor(
    private val qrRepository: QrRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(GenerateUiState())
    val uiState: StateFlow<GenerateUiState> = _uiState.asStateFlow()

    fun onTextChange(text: String) {
        _uiState.value = when (val result = qrRepository.generate(text)) {
            is GenerateQrResult.Success -> GenerateUiState(text = text, qrCode = result.qrCode)
            GenerateQrResult.EmptyContent -> GenerateUiState(text = text)
            GenerateQrResult.ContentTooLong -> GenerateUiState(text = text, isTooLong = true)
        }
    }
}

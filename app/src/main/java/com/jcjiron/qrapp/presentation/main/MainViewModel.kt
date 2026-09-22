package com.jcjiron.qrapp.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jcjiron.qrapp.domain.repository.QrImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: QrImageRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.value = repository.getImage()
                ?.let { MainUiState.ShowingImage(it) }
                ?: MainUiState.Empty()
        }
    }

    fun onImagePicked(uri: String) {
        viewModelScope.launch {
            _uiState.value = try {
                MainUiState.ShowingImage(repository.saveImage(uri))
            } catch (e: IOException) {
                MainUiState.Empty(saveFailed = true)
            } catch (e: SecurityException) {
                MainUiState.Empty(saveFailed = true)
            }
        }
    }

    fun onDeleteImage() {
        viewModelScope.launch {
            repository.deleteImage()
            _uiState.value = MainUiState.Empty()
        }
    }
}

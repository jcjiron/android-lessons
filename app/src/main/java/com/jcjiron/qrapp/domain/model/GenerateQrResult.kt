package com.jcjiron.qrapp.domain.model

sealed interface GenerateQrResult {
    data class Success(val qrCode: QrCode) : GenerateQrResult
    data object EmptyContent : GenerateQrResult
    data object ContentTooLong : GenerateQrResult
}

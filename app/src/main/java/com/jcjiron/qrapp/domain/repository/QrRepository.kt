package com.jcjiron.qrapp.domain.repository

import com.jcjiron.qrapp.domain.model.GenerateQrResult

interface QrRepository {
    fun generate(content: String): GenerateQrResult
}

package com.jcjiron.qrapp.data.repository

import com.google.zxing.WriterException
import com.jcjiron.qrapp.data.qr.ZxingQrEncoder
import com.jcjiron.qrapp.domain.model.GenerateQrResult
import com.jcjiron.qrapp.domain.repository.QrRepository
import javax.inject.Inject

class QrRepositoryImpl @Inject constructor(
    private val encoder: ZxingQrEncoder,
) : QrRepository {

    override fun generate(content: String): GenerateQrResult {
        if (content.isEmpty()) return GenerateQrResult.EmptyContent
        return try {
            GenerateQrResult.Success(encoder.encode(content))
        } catch (e: WriterException) {
            GenerateQrResult.ContentTooLong
        }
    }
}

package com.jcjiron.qrapp.data.repository

import com.jcjiron.qrapp.data.qr.ZxingQrEncoder
import com.jcjiron.qrapp.domain.model.GenerateQrResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class QrRepositoryImplTest {

    private val repository = QrRepositoryImpl(ZxingQrEncoder())

    @Test
    fun `empty text returns EmptyContent`() {
        assertEquals(GenerateQrResult.EmptyContent, repository.generate(""))
    }

    @Test
    fun `text that does not fit returns ContentTooLong`() {
        assertEquals(GenerateQrResult.ContentTooLong, repository.generate("a".repeat(5000)))
    }

    @Test
    fun `valid text returns Success`() {
        assertTrue(repository.generate("hola") is GenerateQrResult.Success)
    }
}

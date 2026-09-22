package com.jcjiron.qrapp.presentation.main

import com.jcjiron.qrapp.domain.model.QrImage
import com.jcjiron.qrapp.domain.repository.QrImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private val repository = FakeQrImageRepository()

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `starts empty when no image was saved`() {
        val viewModel = MainViewModel(repository)
        assertEquals(MainUiState.Empty(), viewModel.uiState.value)
    }

    @Test
    fun `shows the saved image on start`() {
        repository.image = QrImage("/files/qr/saved")
        val viewModel = MainViewModel(repository)
        assertEquals(MainUiState.ShowingImage(QrImage("/files/qr/saved")), viewModel.uiState.value)
    }

    @Test
    fun `picking a photo saves it and shows it`() {
        val viewModel = MainViewModel(repository)
        viewModel.onImagePicked("content://media/picker/1")
        assertEquals("content://media/picker/1", repository.lastSavedUri)
        assertEquals(MainUiState.ShowingImage(repository.image!!), viewModel.uiState.value)
    }

    @Test
    fun `a photo that cannot be copied shows the error`() {
        repository.failOnSave = true
        val viewModel = MainViewModel(repository)
        viewModel.onImagePicked("content://media/picker/1")
        assertEquals(MainUiState.Empty(saveFailed = true), viewModel.uiState.value)
    }

    @Test
    fun `deleting the image goes back to empty`() {
        repository.image = QrImage("/files/qr/saved")
        val viewModel = MainViewModel(repository)
        viewModel.onDeleteImage()
        assertEquals(null, repository.image)
        assertEquals(MainUiState.Empty(), viewModel.uiState.value)
    }
}

private class FakeQrImageRepository : QrImageRepository {
    var image: QrImage? = null
    var failOnSave = false
    var lastSavedUri: String? = null

    override suspend fun getImage(): QrImage? = image

    override suspend fun saveImage(sourceUri: String): QrImage {
        if (failOnSave) throw IOException("boom")
        lastSavedUri = sourceUri
        return QrImage("/files/qr/new").also { image = it }
    }

    override suspend fun deleteImage() {
        image = null
    }
}

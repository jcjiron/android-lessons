package com.jcjiron.qrapp.data.repository

import com.jcjiron.qrapp.data.local.QrImageLocalDataSource
import com.jcjiron.qrapp.domain.model.QrImage
import com.jcjiron.qrapp.domain.repository.QrImageRepository
import javax.inject.Inject

class QrImageRepositoryImpl @Inject constructor(
    private val localDataSource: QrImageLocalDataSource,
) : QrImageRepository {

    override suspend fun getImage(): QrImage? =
        localDataSource.getImageFile()?.let { QrImage(it.absolutePath) }

    override suspend fun saveImage(sourceUri: String): QrImage =
        QrImage(localDataSource.saveImage(sourceUri).absolutePath)

    override suspend fun deleteImage() = localDataSource.deleteImage()
}

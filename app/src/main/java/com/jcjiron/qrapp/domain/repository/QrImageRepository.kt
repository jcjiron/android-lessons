package com.jcjiron.qrapp.domain.repository

import com.jcjiron.qrapp.domain.model.QrImage

interface QrImageRepository {

    /** The saved image, or `null` if the user has not loaded one yet. */
    suspend fun getImage(): QrImage?

    /**
     * Copies the image at [sourceUri] (e.g. from the gallery) into app storage,
     * replacing any previous one.
     */
    suspend fun saveImage(sourceUri: String): QrImage

    suspend fun deleteImage()
}

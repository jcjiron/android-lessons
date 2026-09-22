package com.jcjiron.qrapp.data.local

import android.content.Context
import android.net.Uri
import com.jcjiron.qrapp.di.IoDispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import javax.inject.Inject

/**
 * Keeps a private copy of the QR image in the app's internal storage, so it survives
 * even if the original is deleted from the gallery.
 */
class QrImageLocalDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) {

    private val imagesDir: File
        get() = File(context.filesDir, IMAGES_DIR).apply { mkdirs() }

    suspend fun getImageFile(): File? = withContext(ioDispatcher) {
        imagesDir.listFiles()?.maxByOrNull { it.lastModified() }
    }

    suspend fun saveImage(sourceUri: String): File = withContext(ioDispatcher) {
        // A new name on every save, so image loaders never show a cached old image.
        val target = File(imagesDir, "qr_${System.currentTimeMillis()}")
        val input = context.contentResolver.openInputStream(Uri.parse(sourceUri))
            ?: throw IOException("Could not open $sourceUri")
        input.use { source -> target.outputStream().use { source.copyTo(it) } }
        // Only drop the previous image once the new one is safely written.
        imagesDir.listFiles()?.filter { it != target }?.forEach { it.delete() }
        target
    }

    suspend fun deleteImage() = withContext(ioDispatcher) {
        imagesDir.listFiles()?.forEach { it.delete() }
        Unit
    }

    private companion object {
        const val IMAGES_DIR = "qr"
    }
}

package com.toyprojects.card_pilot.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.toyprojects.card_pilot.domain.repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class ImageRepositoryImpl(
    private val context: Context
) : ImageRepository {

    override suspend fun saveImageFromUri(uriString: String): String = withContext(Dispatchers.IO) {
        val uri = Uri.parse(uriString)
        val fileName = "card_bg_${System.currentTimeMillis()}.jpg"
        val newFile = File(context.filesDir, fileName)

        // 이미지 압축 (Downsampling & JPEG 80%)
        context.contentResolver.openInputStream(uri)?.use { input ->
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeStream(input, null, options)
            
            val reqWidth = 800
            val reqHeight = 800
            var inSampleSize = 1
            
            if (options.outHeight > reqHeight || options.outWidth > reqWidth) {
                val halfHeight: Int = options.outHeight / 2
                val halfWidth: Int = options.outWidth / 2
                while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                    inSampleSize *= 2
                }
            }
            
            val decodeOptions = BitmapFactory.Options().apply {
                this.inSampleSize = inSampleSize
            }
            
            context.contentResolver.openInputStream(uri)?.use { decodeInput ->
                val bitmap = BitmapFactory.decodeStream(decodeInput, null, decodeOptions)
                newFile.outputStream().use { output ->
                    bitmap?.compress(Bitmap.CompressFormat.JPEG, 80, output)
                }
            }
        } ?: throw IllegalArgumentException("Cannot open uri: $uriString")
        
        fileName
    }

    override fun deleteImage(fileName: String) {
        if (fileName.isBlank()) return
        val existingFile = File(context.filesDir, fileName)
        if (existingFile.exists()) {
            existingFile.delete()
        }
    }
}

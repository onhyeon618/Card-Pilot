package com.toyprojects.card_pilot.domain.repository

interface ImageRepository {
    suspend fun saveImageFromUri(uriString: String): String
    fun deleteImage(fileName: String)
}

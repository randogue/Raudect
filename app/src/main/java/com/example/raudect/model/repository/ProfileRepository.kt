package com.example.raudect.model.repository

import android.content.Context
import java.io.File

class ProfileRepository(
    private val appContext: Context
) {
    fun getProfilePictureFile(): File? {
        val file = File(appContext.filesDir, "images/profile_picture.jpg")
        return if (file.exists()) file else null
    }

    fun createProfilePictureFile(): File {
        val dir = File(appContext.filesDir, "images")
        if (!dir.exists()) dir.mkdirs()
        return File(dir, "profile_picture.jpg")
    }
}
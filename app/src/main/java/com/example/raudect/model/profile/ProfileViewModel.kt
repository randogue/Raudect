package com.example.raudect.model.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.raudect.model.repository.FirebaseDatabaseRepository
import com.example.raudect.model.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import java.io.File

class ProfileViewModel(
    private val firebaseDatabaseRepository: FirebaseDatabaseRepository,
    private val profileRepository: ProfileRepository
): ViewModel() {
    //message for ui
    private val _toast = MutableSharedFlow<String>(replay = 0, extraBufferCapacity = 1)
    val toast: SharedFlow<String> = _toast

    //ui for new photo
    private val _photoUri = MutableLiveData<Uri?>()
    val photoUri: LiveData<Uri?> = _photoUri

    //if there is already profile pic
    private val _profilePictureFile = MutableLiveData<File?>()
    val profilePictureFile: LiveData<File?> = _profilePictureFile

    //username from database to display
    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    //form retention
    private val _usernameInput = MutableLiveData<String>()
    val usernameInput: LiveData<String> = _usernameInput

    init {
        firebaseDatabaseRepository.getUserData { result ->
            result
                .onSuccess { username->
                    _username.value = username
                }
                .onFailure { exception ->
                    _toast.tryEmit("Error: ${exception.message}")
                }
        }
    }

    fun setUsernameInput(username:String){
        _usernameInput.value = username
    }

    fun loadProfilePicture() {
        _profilePictureFile.value = profileRepository.getProfilePictureFile()
    }

    fun onPhotoTaken(file: File) {
        _profilePictureFile.value = file
    }

    fun changeUsername(){
        firebaseDatabaseRepository.setUsername(_usernameInput.value ?: "User"){result ->
            result
                .onSuccess {
                    _toast.tryEmit("Successfully change username!")
                    _username.value = _usernameInput.value
                }
                .onFailure { exception ->
                    _toast.tryEmit("Error: ${exception.message}")
                }
        }
    }
}
package com.example.raudect.model.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.raudect.model.repository.FirebaseDatabaseRepository
import com.example.raudect.model.repository.ProfileRepository

class ProfileViewModelFactory(
    private val firebaseDatabaseRepository: FirebaseDatabaseRepository,
    private val profileRepository: ProfileRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ProfileViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(firebaseDatabaseRepository, profileRepository) as T
        }
        else{
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}
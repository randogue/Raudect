package com.example.raudect.model.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.raudect.model.repository.FirebaseAuthRepository

class SignInViewModelFactory(
    private val firebaseAuthRepository: FirebaseAuthRepository,
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>):T{
        if(modelClass.isAssignableFrom(SignInViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return SignInViewModel(firebaseAuthRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
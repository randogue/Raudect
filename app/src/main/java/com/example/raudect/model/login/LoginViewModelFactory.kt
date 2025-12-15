package com.example.raudect.model.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.raudect.model.repository.FirebaseAuthRepository

class LoginViewModelFactory(private val firebaseAuthRepository: FirebaseAuthRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>):T{
        if(modelClass.isAssignableFrom(LoginViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(firebaseAuthRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
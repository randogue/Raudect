package com.example.raudect.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RegisterViewModelFactory(private val initCcnum: String): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>):T{
        if(modelClass.isAssignableFrom(RegisterViewModel::class.java)){
            return RegisterViewModel(initCcnum) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
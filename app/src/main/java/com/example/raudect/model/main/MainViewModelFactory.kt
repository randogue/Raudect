package com.example.raudect.model.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.raudect.model.repository.FirebaseDatabaseRepository

class MainViewModelFactory(private val firebaseDatabaseRepository: FirebaseDatabaseRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>):T{
        if(modelClass.isAssignableFrom(MainViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(firebaseDatabaseRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
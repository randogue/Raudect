package com.example.raudect.model.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.raudect.model.repository.FirebaseDatabaseRepository

class DetailViewModelFactory(
    private val firebaseDatabaseRepository: FirebaseDatabaseRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return DetailViewModel(firebaseDatabaseRepository) as T
        }
        else{
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}
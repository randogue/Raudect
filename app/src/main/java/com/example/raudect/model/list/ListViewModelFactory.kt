package com.example.raudect.model.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.raudect.model.repository.FirebaseDatabaseRepository

class ListViewModelFactory(
    private val firebaseDatabaseRepository: FirebaseDatabaseRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ListViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return ListViewModel(firebaseDatabaseRepository) as T
        }
        else{
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
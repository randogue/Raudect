package com.example.raudect.model.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.raudect.model.repository.FirebaseDatabaseRepository

class EditorViewModelFactory(
    private val firebaseDatabaseRepository: FirebaseDatabaseRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(EditorViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return EditorViewModel(firebaseDatabaseRepository) as T
        }
        else{
            throw IllegalArgumentException("Unknown Viewmodel Class")
        }
    }
}
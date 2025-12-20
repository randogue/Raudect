package com.example.raudect.model.input

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.raudect.model.repository.FirebaseDatabaseRepository
import com.example.raudect.model.repository.FirebaseMachineLearningRepository

class InputRelatedViewModelFactory(
    private val firebaseDatabaseRepository: FirebaseDatabaseRepository,
    private val firebaseMachineLearningRepository: FirebaseMachineLearningRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return when{
            modelClass.isAssignableFrom(InputViewModel::class.java)->
                InputViewModel(firebaseDatabaseRepository) as T
            modelClass.isAssignableFrom(InputTransactionViewModel::class.java)->
                InputTransactionViewModel(firebaseDatabaseRepository,firebaseMachineLearningRepository) as T
            modelClass.isAssignableFrom(RegisterViewModel::class.java)->
                RegisterViewModel(firebaseDatabaseRepository) as T
            else ->
                throw IllegalArgumentException("Unknown Viewmodel Class")
        }
    }
}
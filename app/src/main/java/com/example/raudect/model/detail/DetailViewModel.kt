package com.example.raudect.model.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.raudect.model.ListModel
import com.example.raudect.model.repository.FirebaseDatabaseRepository

class DetailViewModel(
    private val firebaseDatabaseRepository: FirebaseDatabaseRepository
): ViewModel() {
    private val _data = MutableLiveData<ListModel>()
    val data: LiveData<ListModel> = _data

    fun loadDataFromTransactionId(transactionId: String){
        firebaseDatabaseRepository.getTransactionDataById(transactionId){result ->
            result
                .onSuccess { data ->
                    _data.value = data
                }
                .onFailure { exception ->
                    throw exception
                }
        }
    }
}
package com.example.raudect.model.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.raudect.model.ListModel
import com.example.raudect.model.repository.FirebaseDatabaseRepository

class ListViewModel(
    private val firebaseDatabaseRepository: FirebaseDatabaseRepository
): ViewModel() {
    //observable var
    private val _list = MutableLiveData<List<ListModel>>()
    val list: LiveData<List<ListModel>> = _list

    //viewmodel's var
    private val _currentUser = firebaseDatabaseRepository.getCurrentUser()
    private val _cardOwnerRef = firebaseDatabaseRepository.getCardRef()
    private val _transactionRef = firebaseDatabaseRepository.getTransactionRef()

    //list function
    private fun clearList(){
        _list.value = emptyList<ListModel>()
    }
    private fun setList(list: List<ListModel>){
        _list.postValue(list)
    }

    //check list transaction related/managed to user
    fun checkList(){
        //query
        clearList()
        firebaseDatabaseRepository.getTransactionListFromUid(_currentUser?.uid){result ->
            result.onSuccess { list ->
                setList(list)
            }.onFailure { exception ->
                throw exception
            }
        }
    }
}
package com.example.raudect.model.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.raudect.model.repository.FirebaseDatabaseRepository

class MainViewModel(private val firebaseDatabaseRepository: FirebaseDatabaseRepository): ViewModel() {
    //shared variable
    private val _selectTransactionId = MutableLiveData<String>()
    val selectTransactionId: LiveData<String> = _selectTransactionId
    private val _selectCardNum = MutableLiveData<String>()
    val selectCardNum: LiveData<String> = _selectCardNum

    //activity's variable
    private val _currentUser = firebaseDatabaseRepository.getCurrentUser()
    private val _userRef = firebaseDatabaseRepository.getUserRef()

    private val _loginState = MutableLiveData<Boolean>()
    val loginState: LiveData<Boolean> = _loginState

    //save transactionId into vm for sharing between fragment
    fun setSelectTransactionId(transactionId: String){
        _selectTransactionId.value = transactionId
    }

    //save cardNum blah3, shared variable for inputfragment area section
    fun setSelectCardNum(cardNum:String){
        _selectCardNum.value = cardNum
    }

    //record first time user into database
    //contain throwable Exception
    fun checkUserInfoInDatabase(){
        firebaseDatabaseRepository.getQuery(_userRef, _currentUser?.uid){result ->
            result.onSuccess {
                val snapshot = it
                if(!snapshot.exists()){
                    val userInfo =
                        mapOf<String, Any?>(
                            "username" to _currentUser?.displayName,
                            "uid" to _currentUser?.uid
                        )
                    firebaseDatabaseRepository.setQuery(_userRef, _currentUser?.uid, userInfo){result ->
                        result.onSuccess {
                            _loginState.value = true
                        }.onFailure { exception ->
                            throw exception
                        }
                    }
                }
            }.onFailure {
                throw it
            }
        }
    }
}
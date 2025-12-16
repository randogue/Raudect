package com.example.raudect.model.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.raudect.model.repository.FirebaseDatabaseRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference

class MainViewModel(private val firebaseDatabaseRepository: FirebaseDatabaseRepository): ViewModel() {
    private val _currentUser = firebaseDatabaseRepository.getCurrentUser()
    private val _userRef = firebaseDatabaseRepository.getUserRef()

    private val _loginState = MutableLiveData<Boolean>()
    val loginState: LiveData<Boolean> = _loginState

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
package com.example.raudect.model.login

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import com.example.raudect.model.repository.FirebaseAuthRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

class LoginViewModel(
    private val firebaseAuthRepository: FirebaseAuthRepository
) : ViewModel() {
    private val _currentUser = MutableLiveData<FirebaseUser>()
    val currentUser: LiveData<FirebaseUser> = _currentUser

    //delayed init
    init {
        Handler(Looper.getMainLooper()).postDelayed({checkUser()},1200)
    }

    fun checkUser(){
        _currentUser.postValue(firebaseAuthRepository.getCurrentUser())
    }
}
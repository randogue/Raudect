package com.example.raudect.model.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.raudect.model.repository.FirebaseAuthRepository
import com.google.firebase.auth.FirebaseUser

class SignInViewModel(
    private val firebaseAuthRepository: FirebaseAuthRepository
): ViewModel() {
    //variables
    private val _user = MutableLiveData<FirebaseUser>()
    val user: LiveData<FirebaseUser> = _user

    private val _logInSuccess = MutableLiveData<Boolean>()
    val logInSuccess: LiveData<Boolean> = _logInSuccess

    private val _eMessage = MutableLiveData<String>()
    val eMessage: LiveData<String> = _eMessage

    //manage ui state
    fun firebaseAuthWithGoogle(idToken: String){
        //use google auth token to get firebase auth credential
        firebaseAuthRepository.signInWithGoogle(idToken){result ->
            result
                .onSuccess {
                    _user.value = it
                    _logInSuccess.value = true
                }
                .onFailure {
                    _logInSuccess.value = false
                    _eMessage.value = it.message
                }
        }
    }
}
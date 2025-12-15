package com.example.raudect.model.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class FirebaseAuthRepository {
    val auth = FirebaseAuth.getInstance()

    fun getCurrentUser() = auth.currentUser

    fun signInWithGoogle(
        idToken: String,
        callback: (Result<FirebaseUser?>)-> Unit
    ){
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener{task ->
                if(task.isSuccessful){
                    callback(Result.success(auth.currentUser))
                }
                else{
                    callback(Result.failure(task.exception ?: Exception("Unknown error")))
                }
            }
    }

}
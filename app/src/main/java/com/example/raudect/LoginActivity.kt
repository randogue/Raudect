package com.example.raudect

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.raudect.model.login.LoginViewModel
import com.example.raudect.model.login.LoginViewModelFactory
import com.example.raudect.model.repository.FirebaseAuthRepository

class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(FirebaseAuthRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        loginViewModel.currentUser.observe(this){ currentUser->
            if(currentUser != null){
                startActivity(Intent(this, MainActivity::class.java))
            }
            else{
                startActivity((Intent(this, SignInActivity::class.java)))
            }
        }
    }
}
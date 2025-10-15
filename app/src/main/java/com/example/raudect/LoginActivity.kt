package com.example.raudect

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        val users = auth.currentUser

//        val bundle = intent.extras
//        if(bundle != null){
//            users = bundle.getParcelable<FirebaseUser>("user")
//        }

        Handler(Looper.getMainLooper()).postDelayed({
            if(users != null){
                val mainIntent = Intent(this, MainActivity::class.java)
                startActivity(mainIntent)
                finish()
            }
            else{
                val signInIntent = Intent(this, SignInActivity::class.java)
                startActivity(signInIntent)
                finish()
            }
        }, 2000)
    }
}
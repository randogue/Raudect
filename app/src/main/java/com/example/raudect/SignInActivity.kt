package com.example.raudect

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.raudect.model.repository.FirebaseAuthRepository
import com.example.raudect.model.signin.SignInViewModel
import com.example.raudect.model.signin.SignInViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException

class SignInActivity : AppCompatActivity() {
    private val RC_SIGN_IN = 9001

    private val signInViewModel: SignInViewModel by viewModels {
        SignInViewModelFactory(FirebaseAuthRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_in)

        //set google sign in client, for intent, pop up, etc
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        //onclick to start google sign in client intent
        findViewById<SignInButton>(R.id.signIn_button_signInButton).setOnClickListener{
            startActivityForResult(googleSignInClient.signInIntent, RC_SIGN_IN)
        }

        //observing ui state
        signInViewModel.logInSuccess.observe(this){ logInSuccess ->
            if(logInSuccess){
                Toast.makeText(this, "Sign-in Success!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
            }
            else{
                Toast.makeText(this,"Sign-in failed: ${signInViewModel.eMessage.value}", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode:Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                signInViewModel.firebaseAuthWithGoogle(account.idToken!!)
            } catch(e: ApiException){
                Log.w("GoogleSignIn", "Google sign in failed", e)
                Toast.makeText(this,"Sign-in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
package com.example.raudect

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class SignInActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_in)

        auth = FirebaseAuth.getInstance()

        //configure google sign-in
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this,gso)

        //setting up on click button
        findViewById<SignInButton>(R.id.signIn_button_signInButton).setOnClickListener {
            signInWithGoogle()
        }


    }

    private fun signInWithGoogle(){
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode:Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode ==RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch(e: ApiException){
                Log.w("GoogleSignIn", "Google sign in failed", e)
                Toast.makeText(this,"Sign-in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String){
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task->
                if(task.isSuccessful){
                    val user = auth.currentUser
                    Toast.makeText(this, "Sign-in Success!", Toast.LENGTH_SHORT).show()

//                    val bundle = Bundle()
//                    bundle.putParcelable("user", user)

                    val loginIntent = Intent(this, LoginActivity::class.java)
//                    loginIntent.putExtra("user", bundle)
                    startActivity(loginIntent)
                    finish()
                }
                else{
                    Toast.makeText(this,"Sign-in failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
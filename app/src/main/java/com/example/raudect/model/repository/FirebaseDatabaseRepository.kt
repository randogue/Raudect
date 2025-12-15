package com.example.raudect.model.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FirebaseDatabaseRepository {
    //there's many query requiring the uid of the current user for security purpose
    val auth = FirebaseAuth.getInstance()
    val dbRef = FirebaseDatabase.getInstance()

    //ref
    val getUserRef = dbRef.getReference("users")
    val getCardRef = dbRef.getReference("card_owner_info")
    val getTransactionRef = dbRef.getReference("transaction_info")

    //getter setter
    fun getCurrentUser() = auth.currentUser

    //querys
}
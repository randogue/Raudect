package com.example.raudect.model.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FirebaseDatabaseRepository {
    //there's many query requiring the uid of the current user for security purpose
    val auth = FirebaseAuth.getInstance()
    val dbRef = FirebaseDatabase.getInstance()

    //ref
    fun getUserRef() = dbRef.getReference("users")
    fun getCardRef() = dbRef.getReference("card_owner_info")
    fun getTransactionRef() = dbRef.getReference("transaction_info")

    //getter setter
    fun getCurrentUser() = auth.currentUser

    //querys
    //get a row at target (identifiable by key), then callback
    fun getQuery(ref: DatabaseReference, key: String?, callback: (Result<DataSnapshot>) -> Unit){
        if(!key.isNullOrBlank()){
            ref.child(key.orEmpty()).get().addOnSuccessListener { dataSnapshot ->
                callback(Result.success(dataSnapshot))
            }.addOnFailureListener {
                callback(Result.failure(it))
            }
        }
        else{
            callback(Result.failure((Exception("Invalid getQuery Key"))))
        }
    }

    //set value at target row (identifiable by key), then callback
    fun setQuery(ref: DatabaseReference, key:String?, data: Map<String, Any?>, callback: (Result<Unit>) -> Unit){
        if(!key.isNullOrBlank()){
            ref.child(key.orEmpty()).setValue(data).addOnSuccessListener {
                callback(Result.success(Unit))
            }.addOnFailureListener {
                callback(Result.failure(it))
            }
        }
        else{
            callback(Result.failure((Exception("Invalid setQuery Key"))))
        }
    }
}
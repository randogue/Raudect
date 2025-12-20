package com.example.raudect.model.repository

import androidx.compose.ui.graphics.RectangleShape
import com.example.raudect.model.CardModel
import com.example.raudect.model.Indication
import com.example.raudect.model.ListModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.toString

class FirebaseDatabaseRepository {
    //there's many query requiring the uid of the current user for security purpose
    val auth = FirebaseAuth.getInstance()
    val dbRef = FirebaseDatabase.getInstance()

    //ref TODO need to be privatized
    fun getUserRef() = dbRef.getReference("users")
    fun getCardRef() = dbRef.getReference("card_owner_info")
    fun getTransactionRef() = dbRef.getReference("transaction_info")

    //getter setter
    fun getCurrentUser() = auth.currentUser

    //querys
    //get a row at target (identifiable by key), then callback
    fun getQuery(ref: DatabaseReference, key: String?, callback: (Result<DataSnapshot>) -> Unit){
        if(!key.isNullOrBlank()){
            ref.child(key).get().addOnSuccessListener { dataSnapshot ->
                callback(Result.success(dataSnapshot))
            }.addOnFailureListener {
                callback(Result.failure(it))
            }
        }
        else{
            callback(Result.failure((Exception("Invalid getQuery Key"))))
        }
    }

    //get rows where attribute = condition
    fun getQueryWhere(ref: DatabaseReference, attribute:String, condition: String?, callback: (Result<DataSnapshot>) -> Unit){
        if(condition != null){
            ref.orderByChild(attribute).equalTo(condition).get().addOnSuccessListener { dataSnapshot ->
                callback(Result.success(dataSnapshot))
            }.addOnFailureListener { exception ->
                callback(Result.failure(exception))
            }
        }
        else{
            callback(Result.failure(NullPointerException("Condition Is Null")))
        }
    }

    //set value at target row (identifiable by key), then callback
    fun setQuery(ref: DatabaseReference, key:String?, data: Map<String, Any?>, callback: (Result<Unit>) -> Unit){
        if(!key.isNullOrBlank()){
            ref.child(key).setValue(data).addOnSuccessListener {
                callback(Result.success(Unit))
            }.addOnFailureListener {
                callback(Result.failure(it))
            }
        }
        else{
            callback(Result.failure((Exception("Invalid setQuery Key"))))
        }
    }

    //be warned, dont read this section
    //horrendously specific query that requires specific parsing (since parsing is the repository layer)
    fun getTransactionListFromUid(uid:String?, callback: (Result<List<ListModel>>) -> Unit){
        if (uid != null){
            var lists: List<ListModel> = emptyList<ListModel>()
            getQueryWhere(getTransactionRef(), "uid", uid){result ->
                result.onSuccess { dataSnapshot ->
                    for(transaction in dataSnapshot.children){
                        //translate boolean to enum for data model
                        lateinit var indication: Indication
                        if(transaction.child("isfraud").getValue().toString().toBoolean()){
                            indication = Indication.SUSPICIOUS
                        }
                        else{
                            indication = Indication.NORMAL
                        }

                        getQuery(getCardRef(), transaction.child("cardnum").getValue().toString()){result ->
                            result.onSuccess { cardOwner ->
                                val list = ListModel(
                                    transactionId =transaction.child("tid").getValue().toString(),

                                    cardNumber=cardOwner.child("cardnum").getValue().toString(),
                                    dateOfBirth=cardOwner.child("dateofbirth").getValue().toString(),
                                    job=cardOwner.child("job").getValue().toString(),
                                    address=cardOwner.child("address").getValue().toString(),
                                    cityPopulation=cardOwner.child("citypop").getValue().toString(),

                                    transactionTime=transaction.child("date").getValue().toString(),
                                    transactionCategory=transaction.child("category").getValue().toString(),
                                    transactionAmount=transaction.child("amount").getValue().toString(),
                                    transactionLatitude=transaction.child("lat").getValue().toString(),
                                    transactionLongitude=transaction.child("lon").getValue().toString(),
                                    transactionMerchants=transaction.child("merchant").getValue().toString(),
                                    indicator = indication
                                )
                                val currentLists = lists
                                lists = currentLists + list
                                if(dataSnapshot.childrenCount.toInt() == lists.size){
                                    callback(Result.success(lists))
                                }
                            }.onFailure { exception ->
                                callback(Result.failure(exception))
                            }
                        }
                    }
                }.onFailure { exception ->
                    callback(Result.failure(exception))
                }
            }
        }
        else{
            callback(Result.failure(NullPointerException("Uid Is Null")))
        }
    }

    //users CRUD
    fun getUserData(callback: (Result<String>) -> Unit){
        getUserRef().child(getCurrentUser()?.uid.orEmpty()).get()
            .addOnSuccessListener {dataSnapshot ->
                if(dataSnapshot.exists()){
                    callback(Result.success(dataSnapshot.child("username").value.toString()))
                }
                else{
                    callback(Result.failure(Exception("User not found")))
                }
            }
            .addOnFailureListener { exception ->
                callback(Result.failure(exception))
            }
    }

    fun setUsername(username:String, callback: (Result<Unit>) -> Unit){
        getUserRef().child(getCurrentUser()?.uid.orEmpty()).child("username").setValue(username)
            .addOnSuccessListener {
                callback(Result.success(Unit))
            }
            .addOnFailureListener { exception ->
                callback(Result.failure(exception))
            }
    }

    //Create Read Card Data
    fun addCardData(cardNum: String,dateOfBirth:String, job:String, address:String, cityPop: Int, callback: (Result<Unit>) -> Unit){
        getCardDataByCardNum(cardNum){result ->
            result
                .onSuccess { card->
                    if(card.cardNumber == null){
                        val data = mapOf<String, Any>(
                            "cardnum" to cardNum,
                            "dateofbirth" to dateOfBirth,
                            "job" to job,
                            "address" to address,
                            "citypop" to cityPop
                        )
                        getCardRef().child(cardNum).setValue(data)
                            .addOnSuccessListener {
                                callback(Result.success(Unit))
                            }
                            .addOnFailureListener { exception ->
                                callback(Result.failure(exception))
                            }
                    }
                    else{
                        callback(Result.failure(Exception("Card Number Already Exist")))
                    }
                }
                .onFailure { exception ->
                    callback(Result.failure(exception))
                }
        }
    }

    fun getCardDataByCardNum(cardNum: String, callback: (Result<CardModel>) -> Unit){
        getCardRef().child(cardNum).get()
            .addOnSuccessListener { dataSnapshot ->
                if(dataSnapshot.exists()){
                    val data = CardModel(
                        dataSnapshot.child("cardnum").value.toString(),
                        dataSnapshot.child("dateofbirth").value.toString(),
                        dataSnapshot.child("job").value.toString(),
                        dataSnapshot.child("address").value.toString(),
                        dataSnapshot.child("citypop").value.toString().toInt(),
                    )
                    callback(Result.success(data))
                }
                else{
                    val data = CardModel(null,null,null,null,null)
                    callback(Result.success((data)))
                }
            }
            .addOnFailureListener { exception ->
                callback(Result.failure(exception))
            }
    }

    //CRUD Transaction --------------------------------------------------------------------------------------
    fun addTransactionData(cardNum:String, date:String, category:String, amount:Float, lat:Float, lon:Float, merchant:String, isFraud: Boolean, callback: (Result<Unit>) -> Unit){
        val newRowRef = getTransactionRef().push()
        val data = mapOf<String, Any>(
            "tid" to newRowRef.key.toString(),
            "uid" to (getCurrentUser()?.uid ?: ""),
            "cardnum" to cardNum,
            "date" to date,
            "category" to category,
            "amount" to amount,
            "lat" to lat,
            "lon" to lon,
            "merchant" to merchant,
            "isfraud" to isFraud
        )
        newRowRef.setValue(data)
            .addOnSuccessListener {
                callback(Result.success(Unit))
            }
            .addOnFailureListener { exception ->
                callback(Result.failure(exception))
            }
    }

    fun getTransactionDataById(transactionId: String, callback: (Result<ListModel>) -> Unit){
        getTransactionRef().child(transactionId).get()
            .addOnSuccessListener { transaction ->
                if(transaction.exists()){
                    getCardRef().child(transaction.child("cardnum").getValue().toString()).get()
                        .addOnSuccessListener { card ->
                            val date = transaction.child("date").getValue().toString()
                            val category = transaction.child("category").getValue().toString()
                            val amount = transaction.child("amount").getValue().toString()
                            val lat = transaction.child("lat").getValue().toString()
                            val lon = transaction.child("lon").getValue().toString()
                            val merchant = transaction.child("merchant").getValue().toString()
                            val indication = if(transaction.child("isfraud").getValue().toString().toBoolean()) Indication.SUSPICIOUS else Indication.NORMAL

                            val cardNumber = card.child("cardnum").getValue().toString()
                            val dateOfBirth = card.child("dateofbirth").getValue().toString()
                            val job = card.child("job").getValue().toString()
                            val address = card.child("address").getValue().toString()
                            val cityPopulation = card.child("citypop").getValue().toString()

                            val data = ListModel(
                                transactionId,
                                cardNumber,
                                dateOfBirth,
                                job,
                                address,
                                cityPopulation,
                                date,
                                category,
                                amount,
                                lat,
                                lon,
                                merchant,
                                indication
                            )

                            callback(Result.success(data))
                        }
                        .addOnFailureListener { exception ->
                            callback(Result.failure(exception))
                        }
                }
                else{
                    callback(Result.failure(Exception("transaction id doesn't exist in database")))
                }
            }
            .addOnFailureListener {exception ->
                callback(Result.failure(exception))
            }
    }

    fun updateTransactionDataById(transactionId:String, cardNum:String, date:String, category:String, amount:Float, lat:Float, lon:Float, merchant:String, isFraud: Boolean, callback: (Result<Unit>) -> Unit){
        val data = mapOf<String, Any>(
            "cardnum" to cardNum,
            "date" to date,
            "category" to category,
            "amount" to amount,
            "lat" to lat,
            "lon" to lon,
            "merchant" to merchant,
            "isfraud" to isFraud
        )
        getTransactionRef().child(transactionId).updateChildren(data)
            .addOnSuccessListener {
                callback(Result.success(Unit))
            }
            .addOnFailureListener { exception ->
                callback(Result.failure(exception))
            }
    }

    fun deleteTransactionDataById(transactionId: String, callback: (Result<Unit>) -> Unit){
        getTransactionRef().child(transactionId).removeValue()
            .addOnSuccessListener {
                callback(Result.success(Unit))
            }
            .addOnFailureListener { e->
                callback(Result.failure(e))
            }
    }
}
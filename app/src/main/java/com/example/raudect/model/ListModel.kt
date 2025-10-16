package com.example.raudect.model

data class ListModel(
    var transactionId:String,
    var cardNumber:String,
    var dateOfBirth:String,
    var job:String,
    var address:String,
    var cityPopulation:String,
    var transactionTime:String,
    var transactionCategory:String,
    var transactionAmount:String,
    var transactionLatitude:String,
    var transactionLongitude:String,
    var transactionMerchants:String,
    var indicator: Indication
)

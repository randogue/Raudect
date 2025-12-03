package com.example.raudect.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InputTransactionViewModel: ViewModel() {
    private val _tTime = MutableLiveData<String>()
    val tTime = _tTime
    private val _tCategory = MutableLiveData<String>()
    val tCategory = _tCategory
    private val _tAmount = MutableLiveData<String>()
    val tAmount = _tAmount
    private val _tLatitude = MutableLiveData<String>()
    val tLatitude = _tLatitude
    private val _tLongitude = MutableLiveData<String>()
    val tLongitude = _tLongitude
    private val _tMerchant = MutableLiveData<String>()
    val tMerchant = _tMerchant

    init {
        _tTime.postValue("")
        _tCategory.postValue("")
        _tAmount.postValue("")
        _tLatitude.postValue("")
        _tLongitude.postValue("")
        _tMerchant.postValue("")
    }

    fun setTime(input: String){
        _tTime.postValue(input)
    }
    fun setCategory(input: String){
        _tCategory.postValue(input)
    }
    fun setAmount(input: String){
        _tAmount.postValue(input)
    }
    fun setLatitude(input: String){
        _tLatitude.postValue(input)
    }
    fun setLongitude(input: String){
        _tLongitude.postValue(input)
    }
    fun setMerchant(input: String){
        _tMerchant.postValue(input)
    }
}
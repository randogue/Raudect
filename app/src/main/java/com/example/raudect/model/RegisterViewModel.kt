package com.example.raudect.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterViewModel(initString: String): ViewModel() {
    private val _ccnum = MutableLiveData<String>()
    val ccnum: LiveData<String> = _ccnum
    private val _birth = MutableLiveData<String>()
    val birth: LiveData<String> = _birth
    private val _job = MutableLiveData<String>()
    val job: LiveData<String> = _job
    private val _addr = MutableLiveData<String>()
    val addr: LiveData<String> = _addr
    private val _citypop = MutableLiveData<String>()
    val citypop: LiveData<String> = _citypop

    init {
        _ccnum.postValue(initString)
        _birth.postValue("")
        _job.postValue("")
        _addr.postValue("")
        _citypop.postValue("")
    }

    fun setCcnum(input:String){
        _ccnum.postValue(input)
    }
    fun setBirth(input:String){
        _birth.postValue(input)
    }
    fun setJob(input:String){
        _job.postValue(input)
    }
    fun setAddr(input:String){
        _addr.postValue(input)
    }
    fun setCityPop(input:String){
        _citypop.postValue(input)
    }
}
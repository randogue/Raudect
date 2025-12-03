package com.example.raudect.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InputViewModel : ViewModel(){
    private val _ccnum = MutableLiveData<String>()
    val ccnum: LiveData<String> = _ccnum

    init {
        _ccnum.postValue("")
    }

    fun setText(input: String){
        _ccnum.postValue(input)
    }
}
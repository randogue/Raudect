package com.example.raudect.model.input

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.raudect.model.repository.FirebaseDatabaseRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class RegisterViewModel(
    private val firebaseDatabaseRepository: FirebaseDatabaseRepository
): ViewModel() {
    //message to ui
    private val _toast = MutableSharedFlow<String>(replay = 0, extraBufferCapacity = 1)
    val toast: SharedFlow<String> = _toast

    //ui navigation state
    private val _destination = MutableLiveData<String>()
    val destination: LiveData<String> = _destination

    //form retention
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
        _ccnum.postValue("")
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

    fun submitData(){
        firebaseDatabaseRepository.addCardData(
            _ccnum.value.orEmpty(),
            _birth.value.orEmpty(),
            _job.value.orEmpty(),
            _addr.value.orEmpty(),
            _citypop.value?.toInt() ?: 0,
        ){result ->
            result
                .onSuccess {
                    _toast.tryEmit("Successfully added card owner information!")
                    _destination.value = "InputTransaction"
                }
                .onFailure { exception ->
                    _toast.tryEmit("Error: ${exception.message}")
                }
        }
    }
}
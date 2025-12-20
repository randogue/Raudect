package com.example.raudect.model.input

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.raudect.model.repository.FirebaseDatabaseRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class InputViewModel(
    private val firebaseDatabaseRepository: FirebaseDatabaseRepository
): ViewModel() {
    //for sending toast message
    private val _toast = MutableSharedFlow<String>(replay = 0, extraBufferCapacity = 1)
    val toast: SharedFlow<String> = _toast

    //form retention
    private val _cardNum = MutableLiveData<String>()
    val cardNum: LiveData<String> = _cardNum

    //navigation state
    private val _destination = MutableLiveData<String>()
    val destination: LiveData<String> = _destination

    fun setCardNum(cardNum:String){
        _cardNum.value = cardNum
    }

    fun checkCardNum(cardNum: String){
        firebaseDatabaseRepository.getCardDataByCardNum(cardNum){result ->
            result.onSuccess { data->
                if(data.cardNumber != null){
                    _destination.value = "InputTransaction"
                }
                else{
                    if(!_destination.value.isNullOrEmpty()){
                        _destination.value = ""
                    }
                    _destination.value = "Register"
                }
            }
            result.onFailure {exception ->
                _toast.tryEmit("Error: ${exception.message}")
            }
        }
    }
}
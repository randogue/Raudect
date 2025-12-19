package com.example.raudect.model.editor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.raudect.model.ListModel
import com.example.raudect.model.repository.FirebaseDatabaseRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlin.random.Random

class EditorViewModel(
    private val firebaseDatabaseRepository: FirebaseDatabaseRepository
) : ViewModel() {
    private val _data = MutableLiveData<ListModel>()
    val data: LiveData<ListModel> = _data

    //event
    private val _toast = MutableSharedFlow<String>(replay = 0, extraBufferCapacity = 1)
    val toast: SharedFlow<String> = _toast

    //state
    private val _destination = MutableLiveData<String>()
    val destination: LiveData<String> = _destination

    //form retention
    private val _cardNum = MutableLiveData<String>()
    val cardNum: LiveData<String> = _cardNum
    private val _tTime = MutableLiveData<String>()
    val tTime: LiveData<String> = _tTime
    private val _tCategory = MutableLiveData<String>()
    val tCategory: LiveData<String> = _tCategory
    private val _tAmount = MutableLiveData<String>()
    val tAmount: LiveData<String> = _tAmount
    private val _tLatitude = MutableLiveData<String>()
    val tLatitude: LiveData<String> = _tLatitude
    private val _tLongitude = MutableLiveData<String>()
    val tLongitude: LiveData<String> = _tLongitude
    private val _tMerchant = MutableLiveData<String>()
    val tMerchant: LiveData<String> = _tMerchant

    fun loadDataFromTransactionId(transactionId: String){
        firebaseDatabaseRepository.getTransactionDataById(transactionId){result ->
            result
                .onSuccess { data ->
                    _data.value = data
                    _cardNum.value = data.cardNumber
                    _tTime.value = data.transactionTime
                    _tCategory.value = data.transactionCategory
                    _tAmount.value = data.transactionAmount
                    _tLatitude.value = data.transactionLatitude
                    _tLongitude.value = data.transactionLongitude
                    _tMerchant.value = data.transactionMerchants
                }
                .onFailure { exception ->
                    _toast.tryEmit("Error: ${exception.message ?: "Unknown Error"}")
                }
        }
    }

    fun setCardNum(input:String){
        _cardNum.value = input
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

    fun submitChange(){
        firebaseDatabaseRepository.updateTransactionDataById(
            _data.value!!.transactionId,
            _cardNum.value!!,
            _tTime.value!!,
            _tCategory.value!!,
            _tAmount.value!!.toFloat(),
            _tLatitude.value!!.toFloat(),
            _tLongitude.value!!.toFloat(),
            _tMerchant.value!!,
            Random.nextBoolean()//TODO change it with prediction result
        ){result ->
            result.onSuccess{
                _toast.tryEmit("Successfully change transaction data!")
            }
            result.onFailure { exception ->
                _toast.tryEmit("Error: ${exception.message ?: "Unknown Error"}")
            }
        }
    }

    fun deleteData(){
        firebaseDatabaseRepository.deleteTransactionDataById(_data.value!!.transactionId){result ->
            result
                .onSuccess {
                    _destination.value = "List"
                }
                .onFailure { exception ->
                    _toast.tryEmit("Error: ${exception.message ?: "Unknown Error"}")
                }
        }
    }
}
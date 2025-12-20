package com.example.raudect.model.input

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.raudect.model.repository.FirebaseDatabaseRepository
import com.example.raudect.model.repository.FirebaseMachineLearningRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlin.math.PI
import kotlin.math.cos

class InputTransactionViewModel(
    private val firebaseDatabaseRepository: FirebaseDatabaseRepository,
    private val firebaseMachineLearningRepository: FirebaseMachineLearningRepository
): ViewModel() {
    //message for ui
    private val _toast = MutableSharedFlow<String>(replay = 0, extraBufferCapacity = 1)
    val toast: SharedFlow<String> = _toast

    //transaction data creation variable
    private val _cardNum = MutableLiveData<String>()

    //navigation state
    private val _destination = MutableLiveData<String>()
    val destination: LiveData<String> = _destination

    //form retention variable
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

    //form retention's onclick setter
    fun setTime(hour: Int, minute: Int) {
        Log.w("-----------------", "in settime")
        val hourString = hour.toString().padStart(2, '0')
        val minString = minute.toString().padStart(2, '0')
        _tTime.value = "${hourString}:${minString}"
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

    fun loadCardNum(cardNum:String){
        _cardNum.value=cardNum
    }

    fun submitData(){
        checkFraud { result ->
            result
                .onSuccess { isFraud->
                    firebaseDatabaseRepository.addTransactionData(
                        _cardNum.value!!,
                        _tTime.value!!,
                        _tCategory.value!!,
                        _tAmount.value!!.toFloat(),
                        _tLatitude.value!!.toFloat(),
                        _tLongitude.value!!.toFloat(),
                        _tMerchant.value!!,
                        isFraud
                    ){result ->
                        result
                            .onSuccess {
                                _toast.tryEmit("Successfully added the transaction!")
                                _destination.value = "List"
                            }
                            .onFailure {exception ->
                                _toast.tryEmit("Error: ${exception.message}")
                            }
                    }
                }
                .onFailure { exception ->
                    _toast.tryEmit("Error: ${exception.message}")
                }
        }
    }

    fun extractHour(time: String): Int {
        return time.substringBefore(":").toInt()
    }

    fun cosineEncodeHour(hour: Int): Float {
        return cos(2 * PI * hour / 24).toFloat()
    }

    fun checkFraud(callback:(Result<Boolean>)->Unit){
        val input = Array(1){
            floatArrayOf(
                normalize(_tAmount.value!!.toFloat(), 1.0.toFloat(), 28948.9.toFloat()),
                cosineEncodeHour(extractHour(_tTime.value!!.toString())),
                normalize(_tLatitude.value!!.toFloat(), 20.0271.toFloat(), 66.6933.toFloat()),
                normalize(_tLongitude.value!!.toFloat(), -(165.6723.toFloat()), -(67.9503.toFloat()))
            )
        }
        firebaseMachineLearningRepository.getMachineLearningOutput(input){result ->
            result
                .onSuccess { output->
                    callback(Result.success(output))
                }
                .onFailure { exception ->
                    _toast.tryEmit("Error: ${exception.message}")
                }
        }
    }

    fun normalize(x:Float, min:Float, max:Float): Float{
        return ((x-min)/(max-min))
    }
}
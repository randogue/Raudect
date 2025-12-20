package com.example.raudect

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.raudect.model.input.InputRelatedViewModelFactory
import com.example.raudect.model.input.InputTransactionViewModel
import com.example.raudect.model.main.MainViewModel
import com.example.raudect.model.repository.FirebaseDatabaseRepository
import com.example.raudect.model.repository.FirebaseMachineLearningRepository
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.toString

class InputTransactionFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    private val inputTransactionViewModel: InputTransactionViewModel by viewModels {
        InputRelatedViewModelFactory(
            FirebaseDatabaseRepository(),
            FirebaseMachineLearningRepository())
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_input_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //view holding
        val tTime = view.findViewById<TextInputEditText>(R.id.inputTransactionFragment_inputEdit_transactionTime_layout)
        val tCategory = view.findViewById<TextInputEditText>(R.id.inputTransactionFragment_inputEdit_category_layout)
        val tAmount = view.findViewById<TextInputEditText>(R.id.inputTransactionFragment_inputEdit_transactionAmount_layout)
        val tLatitude = view.findViewById<TextInputEditText>(R.id.inputTransactionFragment_inputEdit_transactionLatitude_layout)
        val tLongitude = view.findViewById<TextInputEditText>(R.id.inputTransactionFragment_inputEdit_transactionLongitude_layout)
        val tMerchant = view.findViewById<TextInputEditText>(R.id.inputTransactionFragment_inputEdit_merchantsName_layout)

        //to catch message from vm
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                inputTransactionViewModel.toast.collect { message->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                }
            }
        }

        //load card number from inputfragment or registerfragment through mainViewmodel
        mainViewModel.selectCardNum.observe(viewLifecycleOwner){cardNum->
            inputTransactionViewModel.loadCardNum(cardNum)
        }

        //navigation logic
        inputTransactionViewModel.destination.observe(viewLifecycleOwner){destination->
            if(!destination.isNullOrEmpty()){
                findNavController().navigate(R.id.action_inputTransactionFragment_listFragment)
            }
        }

        //Form retention
        //observe...
        inputTransactionViewModel.tTime.observe(viewLifecycleOwner){
            if(tTime.text.toString() != it){
                tTime.setText(it)
            }
        }
        inputTransactionViewModel.tCategory.observe(viewLifecycleOwner){
            if(tCategory.text.toString() != it){
                tCategory.setText(it)
            }
        }
        inputTransactionViewModel.tAmount.observe(viewLifecycleOwner){
            if(tAmount.text.toString() != it){
                tAmount.setText(it)
            }
        }
        inputTransactionViewModel.tLatitude.observe(viewLifecycleOwner){
            if(tLatitude.text.toString() != it){
                tLatitude.setText(it)
            }
        }
        inputTransactionViewModel.tLongitude.observe(viewLifecycleOwner){
            if(tLongitude.text.toString() != it){
                tLongitude.setText(it)
            }
        }
        inputTransactionViewModel.tMerchant.observe(viewLifecycleOwner){
            if(tMerchant.text.toString() != it){
                tMerchant.setText(it)
            }
        }
        //set...
        tCategory.addTextChangedListener {
            inputTransactionViewModel.setCategory(tCategory.text.toString())
        }
        tAmount.addTextChangedListener {
            inputTransactionViewModel.setAmount(tAmount.text.toString())
        }
        tLatitude.addTextChangedListener {
            inputTransactionViewModel.setLatitude(tLatitude.text.toString())
        }
        tLongitude.addTextChangedListener {
            inputTransactionViewModel.setLongitude(tLongitude.text.toString())
        }
        tMerchant.addTextChangedListener {
            inputTransactionViewModel.setMerchant(tMerchant.text.toString())
        }

        //timepicker
        tTime.setOnClickListener {
            val calendar = Calendar.getInstance()

            TimePickerDialog(
                requireContext(),
                { _, hour, minute ->
                    Log.w("------------", "before settime")
                    inputTransactionViewModel.setTime(hour, minute)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true // 24-hour format
            ).show()
        }

        view.findViewById<Button>(R.id.inputTransactionFragment_button_submit).setOnClickListener{
            inputTransactionViewModel.submitData()
        }
    }
}
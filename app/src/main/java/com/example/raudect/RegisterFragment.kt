package com.example.raudect

import android.os.Bundle
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
import com.example.raudect.model.input.RegisterViewModel
import com.example.raudect.model.main.MainViewModel
import com.example.raudect.model.repository.FirebaseDatabaseRepository
import com.example.raudect.model.repository.FirebaseMachineLearningRepository
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import kotlin.toString

class RegisterFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    private val registerViewModel: RegisterViewModel by viewModels {
        InputRelatedViewModelFactory(FirebaseDatabaseRepository(), FirebaseMachineLearningRepository())
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //view holding
        val pCardNumber = view.findViewById<TextInputEditText>(R.id.registerFragment_inputEdit_cardNumber_layout)
        val pDateOfBirth = view.findViewById<TextInputEditText>(R.id.registerFragment_inputEdit_dateOfBirth_layout)
        val pJob = view.findViewById<TextInputEditText>(R.id.registerFragment_inputEdit_job_layout)
        val pAddress = view.findViewById<TextInputEditText>(R.id.registerFragment_inputEdit_address_layout)
        val pCityPopulation = view.findViewById<TextInputEditText>(R.id.registerFragment_inputEdit_cityPopulation_layout)

        //to catch message from vm
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                registerViewModel.toast.collect { message->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                }
            }
        }

        mainViewModel.selectCardNum.observe(viewLifecycleOwner){cardNum->
            registerViewModel.setCcnum(cardNum)
        }

        //form retention
        //observe...
        registerViewModel.ccnum.observe(viewLifecycleOwner){
            if(pCardNumber.text.toString() != it){
                pCardNumber.setText(it)
            }
        }
        registerViewModel.birth.observe(viewLifecycleOwner){
            if(pDateOfBirth.text.toString() != it){
                pDateOfBirth.setText(it)
            }
        }
        registerViewModel.job.observe(viewLifecycleOwner){
            if(pJob.text.toString() != it){
                pJob.setText(it)
            }
        }
        registerViewModel.addr.observe(viewLifecycleOwner){
            if(pAddress.text.toString() != it){
                pAddress.setText(it)
            }
        }
        registerViewModel.citypop.observe(viewLifecycleOwner){
            if(pCityPopulation.text.toString() != it){
                pCityPopulation.setText(it)
            }
        }
        //set...
        pCardNumber.addTextChangedListener {
            registerViewModel.setCcnum(pCardNumber.text.toString())
        }
        pDateOfBirth.addTextChangedListener {
            registerViewModel.setBirth(pDateOfBirth.text.toString())
        }
        pJob.addTextChangedListener {
            registerViewModel.setJob(pJob.text.toString())
        }
        pAddress.addTextChangedListener {
            registerViewModel.setAddr(pAddress.text.toString())
        }
        pCityPopulation.addTextChangedListener {
            registerViewModel.setCityPop(pCityPopulation.text.toString())
        }

        //set on click at submit button
        view.findViewById<Button>(R.id.registerFragment_button_submit).setOnClickListener{
            registerViewModel.submitData()
        }

        //navigation logic
        registerViewModel.destination.observe(viewLifecycleOwner){
            mainViewModel.setSelectCardNum(pCardNumber.text.toString())
            findNavController().navigate(R.id.action_registerFragment_inputTransactionFragment)
        }
    }
}
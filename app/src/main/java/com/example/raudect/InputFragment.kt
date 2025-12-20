package com.example.raudect

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.ContextThemeWrapper
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
import com.example.raudect.model.input.InputViewModel
import com.example.raudect.model.main.MainViewModel
import com.example.raudect.model.repository.FirebaseDatabaseRepository
import com.example.raudect.model.repository.FirebaseMachineLearningRepository
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class InputFragment : Fragment() {
    private var themedContext: Context? = null

    private val mainViewModel: MainViewModel by activityViewModels()
    private val inputViewModel: InputViewModel by viewModels {
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
        //return inflater.inflate(R.layout.fragment_input, container, false)

        //create custom inflater to apply different theme
        val contextThemeWrapper = ContextThemeWrapper(activity, R.style.Base_Theme_Raudect)
        val themedInflater = inflater.cloneInContext(contextThemeWrapper)
        return themedInflater.inflate(R.layout.fragment_input, container, false)
        //ContextThemeWrapper is basically a class that allows u to wrap a new theme to an existing context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //input view holding
        //personal
        val pCardNumber = view.findViewById<TextInputEditText>(R.id.inputFragment_inputEdit_cardNumber_layout)

        //to catch message from vm
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                inputViewModel.toast.collect { message->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                }
            }
        }

        //form retention
        //Observe...
        inputViewModel.cardNum.observe(viewLifecycleOwner){
            if(pCardNumber.text.toString() != it){
                pCardNumber.setText(it)
            }
        }
        //set...
        pCardNumber.addTextChangedListener{
            inputViewModel.setCardNum(pCardNumber.text.toString())
        }

        //set on click for checking if card number exist in database
        view.findViewById<Button>(R.id.inputFragment_button_submit)
            .setOnClickListener {
                inputViewModel.checkCardNum(pCardNumber.text.toString())
            }

        //navigation logic
        inputViewModel.destination.observe(viewLifecycleOwner){destination->
            if(!destination.isNullOrEmpty()){
                if(destination == "InputTransaction"){
                    mainViewModel.setSelectCardNum(pCardNumber.text.toString())
                    findNavController().navigate(R.id.action_inputFragment_inputTransactionFragment)
                }
                if(destination == "Register"){
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Information not Found")
                    builder.setMessage("Register card number information?")

                    //when yes
                    builder.setPositiveButton("Yes"){dialog, _->
                        mainViewModel.setSelectCardNum(pCardNumber.text.toString())
                        findNavController().navigate(R.id.action_inputFragment_registerFragment)
                        dialog.dismiss()
                    }

                    //when no
                    builder.setNegativeButton("No"){dialog, _->
                        dialog.dismiss()
                    }

                    builder.show()
                }
            }
        }

    }
}
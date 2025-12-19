package com.example.raudect

import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.raudect.model.editor.EditorViewModel
import com.example.raudect.model.editor.EditorViewModelFactory
import com.example.raudect.model.main.MainViewModel
import com.example.raudect.model.repository.FirebaseDatabaseRepository
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch


class EditorFragment : Fragment() {
    //get viewmodel from activity, make editor fragment's vm
    private val mainViewModel: MainViewModel by activityViewModels()
    private val editorViewModel: EditorViewModel by viewModels {
        EditorViewModelFactory(FirebaseDatabaseRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val contextThemeWrapper = ContextThemeWrapper(activity, R.style.Base_Theme_Raudect)
        val inflaterThemed = inflater.cloneInContext(contextThemeWrapper)
        return inflaterThemed.inflate(R.layout.fragment_editor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //view holding
        val cardNumber = view.findViewById<TextInputEditText>(R.id.editorFragment_cardNumber_id)
        val date = view.findViewById<TextInputEditText>(R.id.editorFragment_transactionTime_id)
        val category = view.findViewById<TextInputEditText>(R.id.editorFragment_category_id)
        val amount = view.findViewById<TextInputEditText>(R.id.editorFragment_transactionAmount_id)
        val lat = view.findViewById<TextInputEditText>(R.id.editorFragment_transactionLatitude_id)
        val lon = view.findViewById<TextInputEditText>(R.id.editorFragment_transactionLongitude_id)
        val merchant = view.findViewById<TextInputEditText>(R.id.editorFragment_transactionMerchant_id)

        val submit = view.findViewById<Button>(R.id.editorFragment_button_submit)

        //to catch message from vm
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                editorViewModel.toast.collect { message->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                }
            }
        }

        //load shared tid to get transaction data in vm
        mainViewModel.selectTransactionId.observe(viewLifecycleOwner){transactionId->
            editorViewModel.loadDataFromTransactionId(transactionId)
        }

        //observe navigation state
        editorViewModel.destination.observe(viewLifecycleOwner){destination->
            if(destination == "List"){
                findNavController().navigate(R.id.action_individualFragment_listFragment)
            }
        }

        //form retention
        //observe...
        editorViewModel.cardNum.observe(viewLifecycleOwner){
            if(cardNumber.text.toString() != it){
                cardNumber.setText(it)
            }
        }
        editorViewModel.tTime.observe(viewLifecycleOwner){
            if(date.text.toString() != it){
                date.setText(it)
            }
        }
        editorViewModel.tCategory.observe(viewLifecycleOwner){
            if(category.text.toString() != it){
                category.setText(it)
            }
        }
        editorViewModel.tAmount.observe(viewLifecycleOwner){
            if(amount.text.toString() != it){
                amount.setText(it)
            }
        }
        editorViewModel.tLatitude.observe(viewLifecycleOwner){
            if(lat.text.toString() != it){
                lat.setText(it)
            }
        }
        editorViewModel.tLongitude.observe(viewLifecycleOwner){
            if(lon.text.toString() != it){
                lon.setText(it)
            }
        }
        editorViewModel.tMerchant.observe(viewLifecycleOwner){
            if(merchant.text.toString() != it){
                merchant.setText(it)
            }
        }
        //set...
        cardNumber.addTextChangedListener {
            editorViewModel.setCardNum(cardNumber.text.toString())
        }
        date.addTextChangedListener {
            editorViewModel.setTime(date.text.toString())
        }
        category.addTextChangedListener {
            editorViewModel.setCategory(category.text.toString())
        }
        amount.addTextChangedListener {
            editorViewModel.setAmount(amount.text.toString())
        }
        lat.addTextChangedListener {
            editorViewModel.setLatitude(lat.text.toString())
        }
        lon.addTextChangedListener {
            editorViewModel.setLongitude(lon.text.toString())
        }
        merchant.addTextChangedListener {
            editorViewModel.setMerchant(merchant.text.toString())
        }

        //setting onclick for submitting changes
        submit.setOnClickListener {
            editorViewModel.submitChange()
        }

        //setting onclick for deleting transaction
        view.findViewById<Button>(R.id.editorFragment_button_delete)
            .setOnClickListener {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Deleting a Transaction Data")
                builder.setMessage("Are you sure you want to delete this data?")
                builder.setPositiveButton("Yes"){ dialog, _->
                    editorViewModel.deleteData()
                    dialog.dismiss()
                }

                builder.setNegativeButton("No"){dialog, _->
                    dialog.dismiss()
                }

                builder.show()
            }
    }
}
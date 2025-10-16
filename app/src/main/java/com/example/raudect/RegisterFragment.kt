package com.example.raudect

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.toString

class RegisterFragment : Fragment() {
    //bundle init
    private var cardNumber:String? = null

    //db + auth init
    private lateinit var cardOwnerRef: DatabaseReference
    private lateinit var auth: FirebaseAuth




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            cardNumber = it.getString("cardnum")
        }
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

        //instantiation
        cardOwnerRef = FirebaseDatabase.getInstance().getReference("card_owner_info")
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        //view holding
        val pCardNumber = view.findViewById<TextInputEditText>(R.id.registerFragment_inputEdit_cardNumber_layout)
        val pDateOfBirth = view.findViewById<TextInputEditText>(R.id.registerFragment_inputEdit_dateOfBirth_layout)
        val pJob = view.findViewById<TextInputEditText>(R.id.registerFragment_inputEdit_job_layout)
        val pAddress = view.findViewById<TextInputEditText>(R.id.registerFragment_inputEdit_address_layout)
        val pCityPopulation = view.findViewById<TextInputEditText>(R.id.registerFragment_inputEdit_cityPopulation_layout)

        //if card number got passed
        if(!cardNumber.isNullOrBlank()){
            pCardNumber.setText(cardNumber)
        }

        //set on click at submit button
        view.findViewById<Button>(R.id.registerFragment_button_submit).setOnClickListener {
            val inputCardNumber = pCardNumber.text.toString()
            val inputDateOfBirth = pDateOfBirth.text.toString()
            val inputJob = pJob.text.toString()
            val inputAddress = pAddress.text.toString()
            val inputCityPopulation = pCityPopulation.text.toString().toFloatOrNull()

            //check if cardnumber already exist
            cardOwnerRef.child(inputCardNumber).get()
                .addOnSuccessListener { result->
                    if(result.exists()){
                        Toast.makeText(context, "Failed to register, card number already registered", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        val data = mapOf(
                            "cardnum" to inputCardNumber,
                            "dateofbirth" to inputDateOfBirth,
                            "job" to inputJob,
                            "address" to inputAddress,
                            "citypop" to inputCityPopulation
                        )
                        cardOwnerRef.child(inputCardNumber).setValue(data)
                            .addOnSuccessListener {
                                Toast.makeText(context,"Successfully registered", Toast.LENGTH_SHORT).show()
                                val bundle = Bundle()
                                bundle.putString("cardnum", inputCardNumber)
                                findNavController().navigate(R.id.action_registerFragment_inputTransactionFragment, bundle)
                            }
                            .addOnFailureListener { e->
                                Toast.makeText(context, "Failed to register, ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                .addOnFailureListener { e->
                    Toast.makeText(context, "Failed to check, ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }


}
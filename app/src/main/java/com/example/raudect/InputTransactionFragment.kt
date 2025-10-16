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
import kotlin.random.Random
import kotlin.toString

class InputTransactionFragment : Fragment() {
    //bundle init
    private var cardNum: String? = null

    //db & auth init
    private lateinit var transactionRef: DatabaseReference
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cardNum = arguments?.getString("cardnum")
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
        //instantiation
        transactionRef = FirebaseDatabase.getInstance().getReference("transaction_info")
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        //view holding
        val tTime = view.findViewById<TextInputEditText>(R.id.inputTransactionFragment_inputEdit_transactionTime_layout)
        val tCategory = view.findViewById<TextInputEditText>(R.id.inputTransactionFragment_inputEdit_category_layout)
        val tAmount = view.findViewById<TextInputEditText>(R.id.inputTransactionFragment_inputEdit_transactionAmount_layout)
        val tLatitude = view.findViewById<TextInputEditText>(R.id.inputTransactionFragment_inputEdit_transactionLatitude_layout)
        val tLongitude = view.findViewById<TextInputEditText>(R.id.inputTransactionFragment_inputEdit_transactionLongitude_layout)
        val tMerchant = view.findViewById<TextInputEditText>(R.id.inputTransactionFragment_inputEdit_merchantsName_layout)

        //set on click for submit
        view.findViewById<Button>(R.id.inputTransactionFragment_button_submit).setOnClickListener {
            //push to create a unique reference
            val newRowRef = transactionRef.push()

            val inputTime = tTime.text.toString()
            val inputCategory = tCategory.text.toString()
            val inputAmount = tAmount.text.toString().toFloat()
            val inputLatitude = tLatitude.text.toString().toFloat()
            val inputLongitude = tLongitude.text.toString().toFloat()
            val inputMerchant = tMerchant.text.toString()
            val inputFraud = Random.nextBoolean()
            val inputTid = newRowRef.key
            val inputUid = user?.uid
            val inputCardNum = cardNum

            val rowData = mapOf(
                "date" to inputTime,
                "category" to inputCategory,
                "amount" to inputAmount,
                "lat" to inputLatitude,
                "lon" to inputLongitude,
                "merchant" to inputMerchant,
                "tid" to inputTid,
                "uid" to inputUid,
                "cardnum" to inputCardNum,
                "isfraud" to inputFraud
            )

            newRowRef.setValue(rowData)
                .addOnSuccessListener {
                    Toast.makeText(context, "Successfully added!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_inputTransactionFragment_listFragment)
                }
                .addOnFailureListener { e->
                    Toast.makeText(context, "Failed to add data: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }

    }
}
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
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class EditorFragment : Fragment() {

    //passed arg init
    //private var transactionId: String? = null ---------------------------------------------------------------------

    //database + auth init
    private lateinit var cardOwnerRef: DatabaseReference
    private lateinit var transactionRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    //input laziest cache xd
    private lateinit var unmutable_tid:String
    private lateinit var unmutable_fraud:String//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

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
//
//        //receiving bundle from individual adapter
//        transactionId = arguments?.getString("tid")//=====================================================
//
//        //view holding
//        val cardNumber = view.findViewById<TextInputEditText>(R.id.editorFragment_cardNumber_id)
//        val date = view.findViewById<TextInputEditText>(R.id.editorFragment_transactionTime_id)
//        val category = view.findViewById<TextInputEditText>(R.id.editorFragment_category_id)
//        val amount = view.findViewById<TextInputEditText>(R.id.editorFragment_transactionAmount_id)
//        val lat = view.findViewById<TextInputEditText>(R.id.editorFragment_transactionLatitude_id)
//        val lon = view.findViewById<TextInputEditText>(R.id.editorFragment_transactionLongitude_id)
//        val merchant = view.findViewById<TextInputEditText>(R.id.editorFragment_transactionMerchant_id)
//
//        val submit = view.findViewById<Button>(R.id.editorFragment_button_submit)
//
//
//        //auth + database instantiation----------------------------------------------------------------------------
//        cardOwnerRef = FirebaseDatabase.getInstance().getReference("card_owner_info")
//        transactionRef = FirebaseDatabase.getInstance().getReference("transaction_info")
//        auth = FirebaseAuth.getInstance()
//        val user = auth.currentUser
//
//        //getting transaction and personal data
//        transactionRef.child(transactionId.toString()).get()
//            .addOnSuccessListener { transaction->
//                if(transaction.exists()){
//                    //filling transaction detail
//                    cardNumber.setText(transaction.child("cardnum").getValue().toString())
//                    date.setText(transaction.child("date").getValue().toString())
//                    category.setText(transaction.child("category").getValue().toString())
//                    amount.setText(transaction.child("amount").getValue().toString())
//                    lat.setText(transaction.child("lat").getValue().toString())
//                    lon.setText(transaction.child("lon").getValue().toString())
//                    merchant.setText(transaction.child("merchant").getValue().toString())
//
//                    unmutable_tid = transaction.child("tid").getValue().toString()
//                    unmutable_fraud = transaction.child("isfraud").getValue().toString()
//                }
//                else{
//                    Toast.makeText(context, "Transaction details not found", Toast.LENGTH_LONG).show()
//                }
//            }
//            .addOnFailureListener { e->
//                Toast.makeText(context, "Error setting item data: ${e.message}", Toast.LENGTH_LONG).show()
//            }
//
//
//        //setting onclick for submitting changes
//        submit.setOnClickListener {
//            val rowUpdateData = mapOf<String, Any>(
//                "cardnum" to cardNumber.text.toString(),
//                "date" to date.text.toString(),
//                "category" to category.text.toString(),
//                "amount" to amount.text.toString().toFloat(),
//                "lat" to lat.text.toString().toFloat(),
//                "lon" to lon.text.toString().toFloat(),
//                "merchant" to merchant.text.toString(),
//            )
//            transactionRef.child(unmutable_tid).updateChildren(rowUpdateData)
//                .addOnSuccessListener {
//                    Toast.makeText(context, "Successfully updated data!", Toast.LENGTH_SHORT).show()
//                }
//                .addOnFailureListener { e->
//                    Toast.makeText(context, "Failed to updated data, ${e.message}", Toast.LENGTH_SHORT).show()
//                }
//        }
//
//        //setting onclick for deleting
//        view.findViewById<Button>(R.id.editorFragment_button_delete)
//            .setOnClickListener {
//                val builder = AlertDialog.Builder(requireContext())
//                builder.setTitle("Deleting a Transaction Data")
//                builder.setMessage("Are you sure you want to delete this data?")
//                builder.setPositiveButton("Yes"){ dialog, _->
//                    transactionRef.child(transactionId.toString()).removeValue()
//                        .addOnSuccessListener {
//                            Toast.makeText(context, "data related to user deleted", Toast.LENGTH_SHORT).show()
//                            findNavController().navigate(R.id.action_individualFragment_listFragment)
//                        }
//                        .addOnFailureListener { e->
//                            Toast.makeText(context, "Failed to delete data: ${e.message}!", Toast.LENGTH_LONG).show()
//                        }
//                    dialog.dismiss()
//                }
//
//                builder.setNegativeButton("No"){dialog, _->
//                    dialog.dismiss()
//                }
//
//                builder.show()
//            }//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    }
}
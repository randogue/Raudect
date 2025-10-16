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
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InputFragment : Fragment() {
    private var themedContext: Context? = null

    //db & auth init
    private lateinit var cardOwnerRef: DatabaseReference
    private lateinit var auth: FirebaseAuth


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
        //db & auth instantiation
        cardOwnerRef = FirebaseDatabase.getInstance().getReference("card_owner_info")
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser


        //input view holding
        //personal
        val pCardNumber = view.findViewById<TextInputEditText>(R.id.inputFragment_inputEdit_cardNumber_layout)


        //set on click for submit
        view.findViewById<Button>(R.id.inputFragment_button_submit)
            .setOnClickListener {
                //the new stuff
                if(pCardNumber.text.toString().isNotBlank()){
                    cardOwnerRef.child(pCardNumber.text.toString()).get()

                        //what to do once the task from get() is finished
                        .addOnSuccessListener { result ->
                            if(result.exists()){
                                val bundle = Bundle()
                                bundle.putString("cardnum", pCardNumber.text.toString())
                                findNavController().navigate(R.id.action_inputFragment_inputTransactionFragment, bundle)
                            }
                            else{//handle cardnumber not found in db
                                //alert dialog
                                val builder = AlertDialog.Builder(context)
                                builder.setTitle("Information not Found")
                                builder.setMessage("Register card number information?")

                                //when yes
                                builder.setPositiveButton("Yes"){dialog, _->
                                    val bundle = Bundle()
                                    bundle.putString("cardnum", pCardNumber.text.toString())
                                    //move to register frag with 'cardnum' bundle
                                    findNavController().navigate(R.id.action_inputFragment_registerFragment, bundle)
                                    dialog.dismiss()
                                }

                                //when no
                                builder.setNegativeButton("No"){dialog, _->
                                    dialog.dismiss()
                                }

                                builder.show()
                            }
                        }
                        .addOnFailureListener { e->
                            Toast.makeText(context, "Query Error: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                }
            }
    }
}
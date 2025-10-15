package com.example.raudect

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.random.Random

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InputFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InputFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var themedContext: Context? = null


    //db & auth init
    private lateinit var fraudTestRef: DatabaseReference
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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
        fraudTestRef = FirebaseDatabase.getInstance().getReference("fraud_test_info")
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser


        //input view holding
        //personal
        val pCardNumber = view.findViewById<TextInputEditText>(R.id.inputFragment_inputEdit_cardNumber_layout)
        val pDateOfBirth = view.findViewById<TextInputEditText>(R.id.inputFragment_inputEdit_dateOfBirth_layout)
        val pJob = view.findViewById<TextInputEditText>(R.id.inputFragment_inputEdit_job_layout)
        val pAddress = view.findViewById<TextInputEditText>(R.id.inputFragment_inputEdit_address_layout)
        val pCityPopulation = view.findViewById<TextInputEditText>(R.id.inputFragment_inputEdit_cityPopulation_layout)
        //transaction
        val tTime = view.findViewById<TextInputEditText>(R.id.inputFragment_inputEdit_transactionTime_layout)
        val tCategory = view.findViewById<TextInputEditText>(R.id.inputFragment_inputEdit_category_layout)
        val tAmount = view.findViewById<TextInputEditText>(R.id.inputFragment_inputEdit_transactionAmount_layout)
        val tLatitude = view.findViewById<TextInputEditText>(R.id.inputFragment_inputEdit_transactionLatitude_layout)
        val tLongitude = view.findViewById<TextInputEditText>(R.id.inputFragment_inputEdit_transactionLongitude_layout)
        val tMerchant = view.findViewById<TextInputEditText>(R.id.inputFragment_inputEdit_merchantsName_layout)


        view.findViewById<Button>(R.id.inputFragment_button_submit).
                setOnClickListener {
                    //data mapping
                    val newRowRef = fraudTestRef.push()
                    val testId = newRowRef.key
                    val rowData = mapOf(
                        "uid" to user?.uid,
                        "tid" to testId,
                        //personal
                        "cardnum" to pCardNumber.text?.toString()?.takeIf{it.isNotBlank()},
                        "dateofbirth" to pDateOfBirth.text?.toString()?.takeIf{it.isNotBlank()},
                        "job" to pJob.text?.toString()?.takeIf{it.isNotBlank()},
                        "address" to pAddress.text?.toString()?.takeIf{it.isNotBlank()},
                        "citypop" to pCityPopulation.text?.toString()?.toFloatOrNull(),
                        //transaction
                        "date" to tTime.text?.toString()?.takeIf{it.isNotBlank()},
                        "category" to tCategory.text?.toString()?.takeIf{it.isNotBlank()},
                        "amount" to tAmount.text?.toString()?.toFloatOrNull(),
                        "lat" to tLatitude.text?.toString()?.toFloatOrNull(),
                        "lon" to tLongitude.text?.toString()?.toFloatOrNull(),
                        "merchant" to tMerchant.text?.toString()?.takeIf{it.isNotBlank()},

                        //result manipulation
                        "isfraud" to Random.nextBoolean()
                    )

                    //setting value
                    newRowRef.setValue(rowData).addOnCompleteListener { task->
                        if(task.isSuccessful){
                            Log.d("INPUT_FRAGMENT","Write success!")
                            Toast.makeText(context, "Successfully added data!", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(
                                R.id.action_inputFragment_listFragment
                            )
                        }
                        else{
                            Log.e("INPUT_FRAGMENT", "Write failed: ${task.exception?.message}")
                            Toast.makeText(context, "Failed to add data...", Toast.LENGTH_SHORT).show()
                        }
                    }

                }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment InputFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InputFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
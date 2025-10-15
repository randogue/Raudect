package com.example.raudect

import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class EditorFragment : Fragment() {

    //passed arg init
    private var testId: String? = null

    //database + auth init
    private lateinit var fraudTestRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    //input laziest cache xd
    private lateinit var unmutable_tid:String
    private lateinit var unmutable_fraud:String

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

        //receiving bundle from individual adapter
        testId = arguments?.getString("tid")

        //view holding
        val cardNumber = view.findViewById<TextInputEditText>(R.id.editorFragment_cardNumber_id)
        val dateOfBirth = view.findViewById<TextInputEditText>(R.id.editorFragment_dateOfBirth_id)
        val job = view.findViewById<TextInputEditText>(R.id.editorFragment_job_id)
        val address = view.findViewById<TextInputEditText>(R.id.editorFragment_address_id)
        val cityPopulation = view.findViewById<TextInputEditText>(R.id.editorFragment_cityPopulation_id)

        val date = view.findViewById<TextInputEditText>(R.id.editorFragment_transactionTime_id)
        val category = view.findViewById<TextInputEditText>(R.id.editorFragment_category_id)
        val amount = view.findViewById<TextInputEditText>(R.id.editorFragment_transactionAmount_id)
        val lat = view.findViewById<TextInputEditText>(R.id.editorFragment_transactionLatitude_id)
        val lon = view.findViewById<TextInputEditText>(R.id.editorFragment_transactionLongitude_id)
        val merchant = view.findViewById<TextInputEditText>(R.id.editorFragment_transactionMerchant_id)

        val submit = view.findViewById<Button>(R.id.editorFragment_button_submit)


        //auth + database instantiation
        fraudTestRef = FirebaseDatabase.getInstance().getReference("fraud_test_info")
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        //getting fraud_test_info row
        fraudTestRef.child(testId.toString()).get().addOnSuccessListener {snapshot ->
            if(snapshot.exists()){

                //filling text view
                cardNumber.setText(snapshot.child("cardnum").getValue().toString())
                dateOfBirth.setText(snapshot.child("dateofbirth").getValue().toString())
                job.setText(snapshot.child("job").getValue().toString())
                address.setText(snapshot.child("address").getValue().toString())
                cityPopulation.setText(snapshot.child("citypop").getValue().toString())

                date.setText(snapshot.child("date").getValue().toString())
                category.setText(snapshot.child("category").getValue().toString())
                amount.setText(snapshot.child("amount").getValue().toString())
                lat.setText(snapshot.child("lat").getValue().toString())
                lon.setText(snapshot.child("lon").getValue().toString())
                merchant.setText(snapshot.child("merchant").getValue().toString())

                unmutable_tid = snapshot.child("tid").getValue().toString()
                unmutable_fraud = snapshot.child("isfraud").getValue().toString()
            }
            else{
                Toast.makeText(context, "Error, accessing nothing", Toast.LENGTH_SHORT).show()
            }

        }.addOnFailureListener { e ->
            Toast.makeText(context, "Firebase Exception: ${e.message}", Toast.LENGTH_SHORT).show()
        }

        //setting onclick for submitting changes
        submit.setOnClickListener {
            val rowUpdateData = mapOf<String, Any>(
                //personal
                "cardnum" to cardNumber.text.toString(),
                "dateofbirth" to dateOfBirth.text.toString(),
                "job" to job.text.toString(),
                "address" to address.text.toString(),
                "citypop" to cityPopulation.text.toString().toInt(),
                //transaction
                "date" to date.text.toString(),
                "category" to category.text.toString(),
                "amount" to amount.text.toString().toFloat(),
                "lat" to lat.text.toString().toFloat(),
                "lon" to lon.text.toString().toFloat(),
                "merchant" to merchant.text.toString(),
            )
            fraudTestRef.child(unmutable_tid).updateChildren(rowUpdateData)
                .addOnSuccessListener {
                    Toast.makeText(context, "Successfully updated data!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e->
                    Toast.makeText(context, "Failed to updated data, ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    companion object {
        fun newInstance(testId: String?): EditorFragment {
            val fragment = EditorFragment()
            val bundle = Bundle()
            bundle.putString("tid", testId)
            fragment.arguments = bundle
            return fragment
        }
    }
}
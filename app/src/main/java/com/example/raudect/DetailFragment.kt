package com.example.raudect

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class DetailFragment : Fragment() {
    //passed arg init
    private var testId: String? = null

    //database + auth init
    private lateinit var fraudTestRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //receiving bundle from individual adapter
        testId = arguments?.getString("tid")

        //view holding
        val cardNumber = view.findViewById<TextView>(R.id.detailFragment_cardNumber_id)
        val dateOfBirth = view.findViewById<TextView>(R.id.detailFragment_dateOfBirth_id)
        val job = view.findViewById<TextView>(R.id.detailFragment_job_id)
        val address = view.findViewById<TextView>(R.id.detailFragment_address_id)
        val cityPopulation = view.findViewById<TextView>(R.id.detailFragment_cityPopulation_id)

        val date = view.findViewById<TextView>(R.id.detailFragment_transactionTime_id)
        val category = view.findViewById<TextView>(R.id.detailFragment_category_id)
        val amount = view.findViewById<TextView>(R.id.detailFragment_transactionAmount_id)
        val lat = view.findViewById<TextView>(R.id.detailFragment_transactionLatitude_id)
        val lon = view.findViewById<TextView>(R.id.detailFragment_transactionLongitude_id)
        val merchant = view.findViewById<TextView>(R.id.detailFragment_transactionMerchant_id)

        fraudTestRef = FirebaseDatabase.getInstance().getReference("fraud_test_info")
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        //getting fraud_test_info row
        fraudTestRef.child(testId.toString()).get().addOnSuccessListener {snapshot ->
            if(snapshot.exists()){

                //filling text view
                cardNumber.text = snapshot.child("cardnum").getValue().toString()
                dateOfBirth.text = snapshot.child("dateofbirth").getValue().toString()
                job.text = snapshot.child("job").getValue().toString()
                address.text = snapshot.child("address").getValue().toString()
                cityPopulation.text = snapshot.child("citypop").getValue().toString()

                date.text = snapshot.child("date").getValue().toString()
                category.text = snapshot.child("category").getValue().toString()
                amount.text = snapshot.child("amount").getValue().toString()
                lat.text = snapshot.child("lat").getValue().toString()
                lon.text = snapshot.child("lon").getValue().toString()
                merchant.text = snapshot.child("merchant").getValue().toString()
            }
            else{
                Toast.makeText(context, "Error, accessing nothing", Toast.LENGTH_SHORT).show()
            }

        }.addOnFailureListener { e ->
            Toast.makeText(context, "Firebase Exception: ${e.message}", Toast.LENGTH_SHORT).show()
        }




    }

    companion object {
        fun newInstance(testId: String?): DetailFragment {
            val fragment = DetailFragment()
            val bundle = Bundle()
            bundle.putString("tid", testId)
            fragment.arguments = bundle
            return fragment
        }
    }

}
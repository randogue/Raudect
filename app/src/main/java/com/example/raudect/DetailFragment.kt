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
    private var transactionId: String? = null

    //database + auth init
    private lateinit var cardOwnerRef: DatabaseReference
    private lateinit var transactionRef: DatabaseReference
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
        transactionId = arguments?.getString("tid")

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

        //auth + db instantiation
        cardOwnerRef = FirebaseDatabase.getInstance().getReference("card_owner_info")
        transactionRef = FirebaseDatabase.getInstance().getReference("transaction_info")
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        //getting transaction and personal data
        transactionRef.child(transactionId.toString()).get()
            .addOnSuccessListener { transaction->
                if(transaction.exists()){
                    //filling transaction detail
                    date.text = transaction.child("date").getValue().toString()
                    category.text = transaction.child("category").getValue().toString()
                    amount.text = transaction.child("amount").getValue().toString()
                    lat.text = transaction.child("lat").getValue().toString()
                    lon.text = transaction.child("lon").getValue().toString()
                    merchant.text = transaction.child("merchant").getValue().toString()

                    //getting personal data
                    cardOwnerRef.child(transaction.child("cardnum").getValue().toString())
                        .get().addOnSuccessListener { card->
                            if(card.exists()){
                                //filling personal detail
                                cardNumber.text = card.child("cardnum").getValue().toString()
                                dateOfBirth.text = card.child("dateofbirth").getValue().toString()
                                job.text = card.child("job").getValue().toString()
                                address.text = card.child("address").getValue().toString()
                                cityPopulation.text = card.child("citypop").getValue().toString()
                            }
                            else{
                                Toast.makeText(context, "Card Owner details not found", Toast.LENGTH_LONG).show()
                            }
                        }
                        .addOnFailureListener { e->
                            Toast.makeText(context, "Error setting item data: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                }
                else{
                    Toast.makeText(context, "Transaction details not found", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener { e->
                Toast.makeText(context, "Error setting item data: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    companion object {
        fun newInstance(transactionId: String?): DetailFragment {
            val fragment = DetailFragment()
            val bundle = Bundle()
            bundle.putString("tid", transactionId)
            fragment.arguments = bundle
            return fragment
        }
    }

}
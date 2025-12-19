package com.example.raudect

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.raudect.model.detail.DetailViewModel
import com.example.raudect.model.detail.DetailViewModelFactory
import com.example.raudect.model.main.MainViewModel
import com.example.raudect.model.repository.FirebaseDatabaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class DetailFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    private val detailViewModel: DetailViewModel by viewModels {
        DetailViewModelFactory(FirebaseDatabaseRepository())
    }

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

        //transactionId from shared, load Listmodel into vm
        mainViewModel.selectTransactionId.observe(viewLifecycleOwner){transactionId ->
            try {
                detailViewModel.loadDataFromTransactionId(transactionId)
            }
            catch (e: Exception){
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

        //binding data from vm to view
        detailViewModel.data.observe(viewLifecycleOwner){ data ->
            cardNumber.text = data.cardNumber
            dateOfBirth.text = data.dateOfBirth
            job.text = data.job
            address.text = data.address
            cityPopulation.text = data.cityPopulation

            date.text = data.transactionTime
            category.text = data.transactionCategory
            amount.text = data.transactionAmount
            lat.text = data.transactionLatitude
            lon.text = data.transactionLongitude
            merchant.text = data.transactionMerchants
        }
    }
}
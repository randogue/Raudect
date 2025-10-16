package com.example.raudect

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.raudect.model.Indication
import com.example.raudect.model.ListModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.Int
import kotlin.String

class ListFragment : Fragment() {
    //connecting item to pager
    private val listAdapter by lazy {
        ListAdapter(
            layoutInflater,
            requireContext(),
            object :
            ListAdapter.OnClickListener {
                //set on click listener
                override fun onItemClick(list: ListModel, position : Int) {
                    //when item element is clicked
                    val transactionId = list.transactionId

                    val bundle = Bundle().apply {
                        putString("tid", transactionId)
                    }
                    findNavController().navigate(R.id.action_listFragment_individualFragment, bundle)
                }
            }
        )
    }

    private lateinit var recyclerView: RecyclerView


    private var contextTheme: Context? = null


    //database, auth, list init
    private lateinit var cardOwnerRef: DatabaseReference
    private lateinit var transactionRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private val list = mutableListOf<ListModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val contextThemeWrapper = ContextThemeWrapper(activity, R.style.Base_Theme_Raudect)
        val themedInflater = inflater.cloneInContext(contextThemeWrapper)
        val view = themedInflater.inflate(R.layout.fragment_list,container, false)

        //set adapter
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.adapter = listAdapter

        //set layout manager
        recyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )

        return view
    } //End of onCreateView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //incase of past memory
        list.clear()
        listAdapter.setData(list)

        //database, auth, user, query instantiation
        cardOwnerRef = FirebaseDatabase.getInstance().getReference("card_owner_info")
        transactionRef = FirebaseDatabase.getInstance().getReference("transaction_info")
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        //getting transaction data & card owner data
        //transaction query
        val query = transactionRef.orderByChild("uid").equalTo(user?.uid)
        query.get().addOnSuccessListener { snapshot ->
            if(snapshot.exists()){
                list.clear()//in case it's not empty

                //row extraction on transaction
                for(transaction in snapshot.children){
                    //translate boolean to enum for data model
                    lateinit var indication: Indication
                    if(transaction.child("isfraud").getValue().toString().toBoolean()){
                        indication = Indication.SUSPICIOUS
                    }
                    else{
                        indication = Indication.NORMAL
                    }

                    cardOwnerRef.child(transaction.child("cardnum").getValue().toString())
                        .get().addOnSuccessListener { cardOwner ->
                            //adding all data
                            list.add(
                                ListModel(
                                    transactionId =transaction.child("tid").getValue().toString(),

                                    cardNumber=cardOwner.child("cardnum").getValue().toString(),
                                    dateOfBirth=cardOwner.child("dateofbirth").getValue().toString(),
                                    job=cardOwner.child("job").getValue().toString(),
                                    address=cardOwner.child("address").getValue().toString(),
                                    cityPopulation=cardOwner.child("citypop").getValue().toString(),

                                    transactionTime=transaction.child("date").getValue().toString(),
                                    transactionCategory=transaction.child("category").getValue().toString(),
                                    transactionAmount=transaction.child("amount").getValue().toString(),
                                    transactionLatitude=transaction.child("lat").getValue().toString(),
                                    transactionLongitude=transaction.child("lon").getValue().toString(),
                                    transactionMerchants=transaction.child("merchant").getValue().toString(),
                                    indicator = indication
                                )
                            )
                            //set data
                            listAdapter.setData(list)
                        }. addOnFailureListener { e->
                            Toast.makeText(context, "Error card owner: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                }
            }
        }.
        addOnFailureListener {e->
            Toast.makeText(context, "Error setting item data: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
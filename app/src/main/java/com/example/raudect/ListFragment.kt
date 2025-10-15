package com.example.raudect

import android.content.Context
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListFragment : Fragment() {
    private val listAdapter by lazy {
        ListAdapter(
            layoutInflater,
            requireContext(),
            object :
            ListAdapter.OnClickListener {
                //set on click listener
                override fun onItemClick(list: ListModel, position : Int) {
                    //when item element is clicked
                    //im still thinking about which method to choose for a similar effect to html GET or POST request.
                    //findNavController().navigate(R.id.actionName, bundle)
                    val bundle = Bundle().apply {
                        putInt("GET_position", position)
                    }
                    findNavController().navigate(R.id.action_listFragment_individualFragment, bundle)
                }
            }
        )
    }

    private lateinit var recyclerView: RecyclerView



    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var contextTheme: Context? = null


    //database, auth, list init
    private lateinit var fraudTestRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private val list = mutableListOf<ListModel>()


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

        //database, auth, user, query instantiation
        fraudTestRef = FirebaseDatabase.getInstance().getReference("fraud_test_info")
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val query = fraudTestRef.orderByChild("uid").equalTo(user?.uid)

        //getting list of data from database
        query.get().addOnSuccessListener { snapshot ->
            if(snapshot.exists()){
                for(child in snapshot.children){
                    //child is equivalent to a row
                    lateinit var indication: Indication
                    if(child.child("isfraud").getValue().toString().toBoolean()){
                        indication = Indication.SUSPICIOUS
                    }
                    else{
                        indication = Indication.NORMAL
                    }

                    list.add(
                        ListModel(
                            cardNumber=child.child("cardnum").getValue().toString(),
                            dateOfBirth=child.child("dateofbirth").getValue().toString(),
                            job=child.child("job").getValue().toString(),
                            address=child.child("address").getValue().toString(),
                            cityPopulation=child.child("citypop").getValue().toString(),
                            transactionTime=child.child("date").getValue().toString(),
                            transactionCategory=child.child("category").getValue().toString(),
                            transactionAmount=child.child("amount").getValue().toString(),
                            transactionLatitude=child.child("lat").getValue().toString(),
                            transactionLongitude=child.child("lon").getValue().toString(),
                            transactionMerchants=child.child("merchant").getValue().toString(),
                            indicator = indication
                        )
                    )
                }
            }
            else{
                //when there is no data
            }
            //set data
            listAdapter.setData(list)
        }.
        addOnFailureListener {e->
            Log.e("Firebase", "Failed: ${e.message}")
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
package com.example.raudect

import android.content.Context
import android.os.Bundle
import android.text.Layout
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.navigation.fragment.findNavController
import com.example.raudect.model.ListModel
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
    private val listData = mutableListOf<ListModel>(
        ListModel(
            cardNumber="1234567890123456",
            dateOfBirth="01/01/2001",
            job="Job1",
            address="Address1",
            cityPopulation="1",
            transactionTime="01/01/2011",
            transactionCategory="Gas",
            transactionAmount="1",
            transactionLatitude="1.1",
            transactionLongitude="1.1",
            transactionMerchants="Merchant1"
        ),
        ListModel(
            cardNumber="0987654321098765",
            dateOfBirth="02/02/2002",
            job="Job2",
            address="Adress2",
            cityPopulation="2",
            transactionTime="02/02/2022",
            transactionCategory="Misc",
            transactionAmount="2",
            transactionLatitude="2.2",
            transactionLongitude="2.2",
            transactionMerchants="Merchant2"
        )
    )
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var contextTheme: Context? = null

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
        //return inflater.inflate(R.layout.fragment_list, container, false)

        val contextThemeWrapper = ContextThemeWrapper(activity, R.style.Base_Theme_Raudect)
        val themedInflater = inflater.cloneInContext(contextThemeWrapper)
        return themedInflater.inflate(R.layout.fragment_list,container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<LinearLayout>(R.id.listFragment_Layout_element1_id).
                setOnClickListener {
                    findNavController().navigate(R.id.action_listFragment_individualFragment)
                }
        view.findViewById<LinearLayout>(R.id.listFragment_Layout_element2_id).
                setOnClickListener {
                    findNavController().navigate(R.id.action_listFragment_individualFragment)
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
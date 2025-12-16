package com.example.raudect

import android.content.Context
import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.raudect.model.ListModel
import com.example.raudect.model.list.ListViewModel
import com.example.raudect.model.list.ListViewModelFactory
import com.example.raudect.model.main.MainViewModel
import com.example.raudect.model.repository.FirebaseDatabaseRepository
import kotlin.Int

class ListFragment : Fragment() {
    //shared activity viewmodel /w IndividualFragment
    private val mainViewModel: MainViewModel by activityViewModels()
    //viewmodel
    private val listViewModel: ListViewModel by viewModels {
        ListViewModelFactory(FirebaseDatabaseRepository())
    }

    //connecting item to pager
    private val listAdapter by lazy {
        ListAdapter(
            layoutInflater,
            requireContext(),
            object :
            ListAdapter.OnClickListener {
                //set on click listener
                override fun onItemClick(list: ListModel, position : Int) {
                    //save transaction id of clicked item into shared vm
                    mainViewModel.setSelectTransactionId(list.transactionId)

                    //when item element is clicked------------------------------------------ use mainviewmodel to share data snapshot instead
                    val transactionId = list.transactionId

                    val bundle = Bundle().apply {
                        putString("tid", transactionId)
                    }//-----------------------------------------------------------------------
                    findNavController().navigate(R.id.action_listFragment_individualFragment, bundle)
                }
            }
        )
    }

    private lateinit var recyclerView: RecyclerView


    private var contextTheme: Context? = null


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

        listAdapter.setData(emptyList<ListModel>())
        listViewModel.list.observe(viewLifecycleOwner){list->
            listAdapter.setData(list)
        }

        try {
            listViewModel.checkList()
        }
        catch (e: Exception){
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
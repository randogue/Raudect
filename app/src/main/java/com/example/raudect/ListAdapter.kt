package com.example.raudect

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.raudect.model.ListModel

//onClickListener gained from ListFragment.kt is type of ListAdapter.OnClickListener interface
class ListAdapter(
    private val layoutInflater: LayoutInflater,
    private val context: Context,
    private val onClickListener: OnClickListener
) : RecyclerView.Adapter<ListViewHolder>()
{
    //List Adapter atr ------------------------------------------------------need to be saved from viewmodel
    private val listOfList = mutableListOf<ListModel>()
    //reason we use val from the lab module might be because they want to add constraint so that to edit the content, we must use mutableList function.

    fun setData(newList: List<ListModel>) {
        listOfList.clear()
        listOfList.addAll(newList)
        notifyDataSetChanged() //tell layout manager to re-apply layout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = layoutInflater.inflate(R.layout.list_element, parent, false)
        return ListViewHolder(view, context, onClickListener)
    }

    override fun getItemCount() = listOfList.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bindData(listOfList[position], position)
    }




    //interface to pass list:ListModel between view holder and list page
    interface OnClickListener {
        fun onItemClick(list: ListModel, position: Int)
    }
}
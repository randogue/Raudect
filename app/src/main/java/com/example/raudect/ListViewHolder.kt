package com.example.raudect

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.raudect.model.Indication
import com.example.raudect.model.ListModel

class ListViewHolder(
    private val containerView: View,
    private val context: Context,
    private val onClickListener: ListAdapter.OnClickListener
) : RecyclerView.ViewHolder(containerView)
{
    //ref to xml's views
    private val cardNumberView : TextView by lazy {
        containerView.findViewById(R.id.list_cardNumber_id)
    }
    private val descriptionView : TextView by lazy {
        containerView.findViewById(R.id.list_description_id)
    }
    private val transactionDateView : TextView by lazy {
        containerView.findViewById(R.id.list_transactionDate_id)
    }
    private val indicatorView : TextView by lazy {
        containerView.findViewById(R.id.list_indicator_id)
    }



    //bind data and connect reference for list:ListModel for onClickListener
    fun bindData(list: ListModel, position : Int) {
        //insert the data to list element
        cardNumberView.text = list.cardNumber
        transactionDateView.text = list.transactionTime
        descriptionView.text = "(Lat:${list.transactionLatitude})(Lon:${list.transactionLongitude})\n${list.transactionMerchants}"
        if(list.indicator == Indication.SUSPICIOUS) {
            indicatorView.text = "SUSPICIOUS"
            indicatorView.setTextColor(context.getColor(R.color.suspicious))
        } else {
            indicatorView.text = "NORMAL"
            indicatorView.setTextColor(context.getColor(R.color.black))
        }

        //put onClickListener.onItemClick to view's set on click (basically communicate through half instantiated interface to call the function in that reference)
        containerView.setOnClickListener {
            onClickListener.onItemClick(list, position)
        }
    }
}
package com.example.raudect

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.raudect.model.TabModel

//contain list of tabs
val TABS_FIXED = listOf(
    TabModel(
        tabTitle = R.string.individualAdapter_tab_detailFragment_string,
        tabFragment = DetailFragment()
    ),
    TabModel(
        tabTitle = R.string.individualAdapter_tab_editorFragment_string,
        tabFragment = EditorFragment()
    )
)

class IndividualAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
): FragmentStateAdapter(fragmentManager, lifecycle)
{

    //returns amount of tabs
    override fun getItemCount(): Int {
        return TABS_FIXED.size
    }

    //create fragment according to it's position
    override fun createFragment(position: Int): Fragment {
        //using parent data
        val theFragment = TABS_FIXED[position].tabFragment
        return theFragment
    }

}
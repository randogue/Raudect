package com.example.raudect

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class IndividualFragment : Fragment(R.layout.fragment_individual) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout with wrapped theme
        val contextThemeWrapper = ContextThemeWrapper(activity, R.style.Theme_NavigationDrawer_NoActionBar)
        val themedInflater = inflater.cloneInContext(contextThemeWrapper)
        return themedInflater.inflate(R.layout.fragment_individual, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //hold view related to pager
        val viewPager = view.findViewById<ViewPager2>(R.id.view_pager)
        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)

        //use adapter to populate tabs
        val adapter = IndividualAdapter(childFragmentManager, lifecycle)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) {tab, position ->
            tab.text = resources.getString(TABS_FIXED[position].tabTitle)
        }.attach()
    }
}
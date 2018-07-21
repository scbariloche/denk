package com.example.andenk.schichtln.gui


import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.andenk.schichtln.R
import com.example.andenk.schichtln.modeladapter.TabsViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_home_base.view.*
import kotlinx.android.synthetic.main.fragment_plan_base.*
import kotlinx.android.synthetic.main.fragment_plan_base.view.*
import org.jetbrains.anko.textView


/**
 * A simple [Fragment] subclass.
 */
class HomeBaseFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_home_base, container, false)
        setupViewPager(view!!.home_viewpager)
        view.home_tabs.setupWithViewPager(view.home_viewpager)
        view.home_tabs.getTabAt(0)!!.icon=activity!!.getDrawable(R.drawable.ic_date_range_black_24dp)
        view.home_tabs.getTabAt(1)!!.icon=activity!!.getDrawable(R.drawable.ic_person_black_24dp)
        view.home_tabs.getTabAt(2)!!.icon=activity!!.getDrawable(R.drawable.ic_swap_horiz_black_24dp)
        return view
    }

    private fun setupViewPager(viewPager: ViewPager) {

        val adapter = TabsViewPagerAdapter(activity!!.supportFragmentManager)
        adapter.addFragment(CalendarFragment(),"Kalender")
        adapter.addFragment(PersonalFragment(), "meine Schichten")
        adapter.addFragment(OpenFragment(), "Tauschbörse")


        viewPager.adapter = null
        viewPager.adapter = adapter
        (viewPager.adapter as TabsViewPagerAdapter).notifyDataSetChanged()
    }

    override fun onPause() {
        super.onPause()
    }
}// Required empty public constructor

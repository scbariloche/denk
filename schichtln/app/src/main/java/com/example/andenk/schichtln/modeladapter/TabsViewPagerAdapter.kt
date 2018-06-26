package com.example.andenk.schichtln.modeladapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

/**
 * Created by andenk on 09.05.2018.
 */
internal class TabsViewPagerAdapter(manager: FragmentManager) : android.support.v4.app.FragmentStatePagerAdapter(manager) {
    private val mFragmentList = mutableListOf<Fragment>()
    private val mFragmentTitleList = mutableListOf<String>()

    override fun getItem(position: Int): Fragment {
        return mFragmentList.get(position)
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)

    }

    override fun getPageTitle(position: Int): CharSequence {
        return mFragmentTitleList.get(position)
    }
}
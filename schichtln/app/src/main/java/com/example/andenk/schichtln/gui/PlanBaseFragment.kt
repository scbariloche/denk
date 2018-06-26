package com.example.andenk.schichtln.gui


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.andenk.schichtln.R
import com.example.andenk.schichtln.getDrawableByName
import com.example.andenk.schichtln.helper.DBHelper
import com.example.andenk.schichtln.modeladapter.TabsViewPagerAdapter
import com.example.andenk.schichtln.webservice.get_positions_by_user
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.fragment_plan_base.view.*


/**
 * A simple [Fragment] subclass.
 */
class PlanBaseFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_plan_base, container, false)

        setupViewPager(view!!)
        view.tabs.setupWithViewPager(view.viewpager)
        return view
    }


    private fun setupViewPager(view: View) {

        get_positions_by_user(user = DBHelper(activity).getCurrentUser()!!, on_success = {
            if (activity != null) {
                val adapter = TabsViewPagerAdapter(activity.supportFragmentManager)
                var index=0
                for (i in it) {
                    FirebaseMessaging.getInstance().subscribeToTopic("position${i.id}")
                    val fragment = Plan_PlanFragment()
                    val args = Bundle()
                    args.putSerializable("position", i)
                    fragment.arguments = args
                    adapter.addFragment(fragment, "${i.name}")
                    try {
                        view.tabs.getTabAt(index)!!.icon=activity.getDrawableByName("${i.name}")
                    }catch (e:Exception){
                        print(e.localizedMessage)
                    }
                    index++
                }


                view.viewpager.adapter = null
                view.viewpager.adapter = adapter
                view.viewpager.adapter.notifyDataSetChanged()
            }
        }, on_error = {
            println(it.message)

        })


    }
}// Required empty public constructor

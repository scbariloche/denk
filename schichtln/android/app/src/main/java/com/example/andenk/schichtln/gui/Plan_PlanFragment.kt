package com.example.andenk.schichtln.gui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.andenk.schichtln.R
import com.example.andenk.schichtln.modeladapter.PositionListAdapter
import com.example.andenk.schichtln.pojos.Group
import com.example.andenk.schichtln.pojos.Schicht
import com.example.andenk.schichtln.webservice.get_all_schicht_by_position
import kotlinx.android.synthetic.main.fragment_position_list.view.*
import org.jetbrains.anko.support.v4.longToast


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Plan_PlanFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Plan_PlanFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Plan_PlanFragment : Fragment() {
    var position = Group()
    var schichtListView: RecyclerView? = null
    var swipeContainer: SwipeRefreshLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view: View = inflater!!.inflate(R.layout.fragment_position_list, container, false)
        schichtListView = view.list_position_schicht
        position = arguments!!.getSerializable("position") as Group
        swipeContainer = view.position_swipe_container


        updateList()
        view.position_swipe_container.setColorScheme(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark)
        view.position_swipe_container.setOnRefreshListener({
            updateList()
        })


        return view
    }


    fun updateList() {
        swipeContainer!!.isRefreshing = true

        get_all_schicht_by_position(position,
                {
                    if (activity != null) {


                        schichtListView?.layoutManager = LinearLayoutManager(activity)
                        schichtListView?.adapter = PositionListAdapter(activity!!, it as List<Schicht>)
                    }
                    swipeContainer!!.isRefreshing = false
                }, {

            longToast("${it.message}")
            swipeContainer!!.isRefreshing = false

        })


    }


}

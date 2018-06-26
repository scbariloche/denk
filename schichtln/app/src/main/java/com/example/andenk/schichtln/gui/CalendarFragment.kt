package com.example.andenk.schichtln.gui

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.Toast
import com.example.andenk.schichtln.R
import com.example.andenk.schichtln.helper.DBHelper
import com.example.andenk.schichtln.helper.createAlert
import com.example.andenk.schichtln.helper.format
import com.example.andenk.schichtln.modeladapter.DetailListAdapter
import com.example.andenk.schichtln.notification.sendMessage
import com.example.andenk.schichtln.pojos.Schicht
import com.example.andenk.schichtln.pojos.Schicht2Change
import com.example.andenk.schichtln.pojos.Type
import com.example.andenk.schichtln.pojos.User
import com.example.andenk.schichtln.webservice.create_schicht
import com.example.andenk.schichtln.webservice.get_all_schicht_by_day
import com.example.andenk.schichtln.webservice.get_all_types_
import com.example.andenk.schichtln.webservice.get_user_by_position
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.fragment_calendar.view.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.support.v4.toast
import org.json.JSONArray
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CalendarFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [CalendarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalendarFragment : Fragment() {

    var cal: Calendar = Calendar.getInstance()
    var ret: View? = null
    val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val SDK_INT = android.os.Build.VERSION.SDK_INT
        if (SDK_INT > 8) {
            val policy = StrictMode.ThreadPolicy.Builder()
                    .permitAll().build()
            StrictMode.setThreadPolicy(policy)

        }
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment


        ret = inflater!!.inflate(R.layout.fragment_calendar, container, false)

        ret!!.calendarView.setOnDateChangeListener(MOnDateChangeListener())

        ret!!.toggle_list_up_down.setOnClickListener(ToggleUpDownListener())
        cal = Calendar.getInstance()
        ret!!.calendarView.date = cal.timeInMillis

        if (DBHelper(activity).getCurrentUser() != null) {

            if (!DBHelper(activity).getCurrentUser()!!.is_superuser) {
                ret!!.btn_add_schicht.visibility = GONE
            }
        } else {
            activity.startActivityForResult(Intent(activity, LoginDialog().javaClass), RESULTCODE_LOGIN)
        }
        ret!!.btn_add_schicht.setOnClickListener(View.OnClickListener {

            openAddSchichtDialog()

        })

        ret!!.btn_today.onClick {
            cal.time = Date(System.currentTimeMillis())
            calendarView.setDate(System.currentTimeMillis(), true, true)
            updateList()

        }
        ret!!.calendar_swipe.setColorScheme(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark)
        ret!!.calendar_swipe.setOnRefreshListener {
            updateList()
        }
        updateList()
        return ret
    }

    private fun openAddSchichtDialog() {
        get_all_types_(on_success = {

            val types: MutableList<Any> = mutableListOf()
            var i = 0
            while (i < it.array().length()) {
                types.add(gson.fromJson(it.array().get(i).toString(), Type::class.java))
                i++
            }
            val schicht = Schicht()

            schicht.day = cal.time
            createAlert(
                    activity,
                    cal.time.format("dd.MM."),
                    types,
                    DialogInterface.OnClickListener(
                            { d: DialogInterface,
                              i_types: Int ->
                                schicht.type = (types.get(i_types) as Type)
                                val users: ArrayList<Any> = ArrayList()
                                val type = types.get(i_types) as Type

                                get_user_by_position(activity, type.position, {
                                    onUsersArrived(it, users, types, i_types, schicht, type)
                                })


                            }
                    )
            ).show()
        }, on_error = {})


    }


    private fun onUsersArrived(it: JSONArray, users: MutableList<Any>, types: MutableList<Any>, i_types: Int, schicht: Schicht, type: Type) {
        var j = 0
        val list = JSONArray(it.toString())
        while (j < list.length()) {
            val user = gson.fromJson(list.get(j).toString(), User::class.java)
            users.add(user)
            j++
        }
        val nameslist = mutableListOf<Any>()
        nameslist.add("OFFEN ANBIETEN")
        nameslist.addAll(users)



        createAlert(
                activity,
                "${(types.get(i_types) as Type).description} am ${cal.time.format("dd.MM.")}",
                nameslist,
                DialogInterface.OnClickListener(
                        { _: DialogInterface,
                          i_users: Int ->
                            if (i_users == 0) {
                                create_schicht(
                                        Schicht2Change(
                                                schicht = schicht,
                                                user_id = DBHelper(activity).getCurrentUser()!!.id,
                                                position_id = type.position.id,
                                                accept_id = 2,
                                                state = "open"
                                        ),
                                        on_success = {
                                            activity.longToast("alle ${type.position.name} werden benachrichtigt")
                                            sendMessage(
                                                    to = "position${type.position.id}",
                                                    title = "${type.description} zu vergeben",
                                                    body = "${type.description} am ${schicht.day.format()}"
                                            )
                                            updateList()
                                        },
                                        on_error = {
                                            toast("${it.message}")
                                            updateList()
                                        }
                                )
                            } else {
                                val user = users[i_users-1] as User
                                schicht.user = user
                                createAlert(
                                        activity,
                                        "${type.description} am ${cal.time.format("dd.MM.yy")} an ${user.username} vergeben?",
                                        DialogInterface.OnClickListener(
                                                { _: DialogInterface,
                                                  _: Int ->
                                                    create_schicht(
                                                            Schicht2Change(
                                                                    schicht = schicht,
                                                                    position_id = type.position.id,
                                                                    accept_id = 2
                                                            ),
                                                            on_success = {
                                                                Toast.makeText(
                                                                        activity,
                                                                        "$user wird benachrichtigt",
                                                                        Toast.LENGTH_SHORT).show()
                                                                sendMessage(
                                                                        to="${user.id}",
                                                                        title = "Du hast eine neue Schicht",
                                                                        body = "${schicht.type.description} am ${schicht.day.format()}"
                                                                )
                                                                updateList()
                                                            },
                                                            on_error = {
                                                                longToast("${it.message}")
                                                            }
                                                    )


                                                })
                                ).show()

                            }
                        }
                )
        ).show()
    }


    fun updateList() {
        ret!!.calendar_swipe.isRefreshing = true
        get_all_schicht_by_day(date = cal.time, on_success = {
            if (activity != null) {
                ret!!.listview_details.layoutManager = LinearLayoutManager(activity)
                ret!!.listview_details.adapter = DetailListAdapter(activity, it as List<Schicht>, { updateList() })

            }
            ret!!.calendar_swipe.isRefreshing = false
        }, on_error = {
            println(it.message)
            ret!!.calendar_swipe.isRefreshing = false
        })


    }

    inner class MOnDateChangeListener : CalendarView.OnDateChangeListener {
        /**
         * Called upon change of the selected day.
         *
         * @param view The view associated with this listener.
         * @param year The year that was set.
         * @param month The month that was set [0-11].
         * @param dayOfMonth The day of the month that was set.
         */
        override fun onSelectedDayChange(view: CalendarView?, year: Int, month: Int, dayOfMonth: Int) {

            cal.set(year, month, dayOfMonth)

            updateList()

        }
    }


    inner class ToggleUpDownListener : View.OnClickListener {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        override fun onClick(v: View?) {
            if (ret!!.calendarView.visibility == View.GONE) {
                ret!!.calendarView.visibility = View.VISIBLE
            } else {
                ret!!.calendarView.visibility = View.GONE
            }


        }
    }


}

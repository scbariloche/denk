package com.example.andenk.schichtln.gui

import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.andenk.schichtln.R
import com.example.andenk.schichtln.format
import com.example.andenk.schichtln.helper.DBHelper
import com.example.andenk.schichtln.helper.createAlert

import com.example.andenk.schichtln.helper.gson
import com.example.andenk.schichtln.modeladapter.PersonalListAdapter
import com.example.andenk.schichtln.notification.sendMessage
import com.example.andenk.schichtln.openLoginDialog
import com.example.andenk.schichtln.pojos.Schicht
import com.example.andenk.schichtln.pojos.Schicht2Change
import com.example.andenk.schichtln.pojos.User
import com.example.andenk.schichtln.webservice.delete_schicht
import com.example.andenk.schichtln.webservice.get_all_schichten_by_user
import com.example.andenk.schichtln.webservice.get_user_by_position
import com.example.andenk.schichtln.webservice.update_schicht
import kotlinx.android.synthetic.main.fragment_personal_open.view.*
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.support.v4.toast
import org.json.JSONArray


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [PersonalFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [PersonalFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PersonalFragment : Fragment() {
    var convertview: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        convertview = inflater!!.inflate(R.layout.fragment_personal_open, container, false)

        convertview!!.swipe_container.setColorScheme(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark)

        convertview!!.swipe_container.setOnRefreshListener({


            updatePersonal()
        })

        updatePersonal()
        return convertview
    }


    fun updatePersonal() {

        if (DBHelper(activity!!).getCurrentUser() != null) {
            convertview!!.swipe_container.isRefreshing = true
            get_all_schichten_by_user(DBHelper(activity!!).getCurrentUser()!!, on_success = {
                convertview!!.list_personal_open.layoutManager = LinearLayoutManager(activity)
                if (activity != null) {
                    convertview!!.list_personal_open.adapter = PersonalListAdapter(
                            context = activity!!,
                            objects = it as List<Schicht>,
                            offer = { offer(it) },
                            set_accepted = { setSchichtAccepted(it) },
                            delete = { deleteSchicht(it) },
                            set_open = { setSchichtOpen(it) },
                            set_for_trade = { setSchichtForTrade(it) },
                            update = { updatePersonal() }
                    )


                    convertview!!.swipe_container.isRefreshing = false
                }
            }, on_error = {
                println(it.message)
                convertview!!.swipe_container.isRefreshing = false

            })
        } else {
            activity!!.openLoginDialog(RESULTCODE_LOGIN)
        }

    }


    private fun offer(item: Schicht) {
        var userList = ArrayList<Any>()
        get_user_by_position(activity!!, item.position, {

            var i = 0
            var jsonArray: JSONArray = it as JSONArray
            print(jsonArray)
            while (i < jsonArray.length()) {
                var schicht = gson.fromJson(jsonArray.get(i).toString(), User::class.java)
                userList.add(schicht)
                i++
            }

            createAlert(activity!!,
                    "",
                    userList,
                    DialogInterface.OnClickListener(
                            { dialogInterface: DialogInterface,
                              i_alert_list: Int ->
                                createAlert(activity!!,
                                        "Die Schicht bleibt solange unter deiner" +
                                                " Verantwortung bis ${(userList.get(i_alert_list) as User).username} diese " +
                                                "Schicht annimmt.",
                                        DialogInterface.OnClickListener(
                                                { d: DialogInterface, i_alert ->

                                                    update_schicht(item, Schicht2Change(
                                                            item,
                                                            state = "offered",
                                                            offered_to_id = (userList.get(i_alert_list) as User).id
                                                    ), on_success = {
                                                        Toast.makeText(
                                                                activity,
                                                                "${(userList.get(i_alert_list) as User).username} wird benachrichtigt",
                                                                Toast.LENGTH_SHORT).show()
                                                        sendMessage(
                                                                to = "${(userList.get(i_alert_list) as User).id}",
                                                                title = "Kannst du am ${item.day.format()}",
                                                                body = "${item.user!!.username} bietet dir '${item.type.description}' an"
                                                        )

                                                        updatePersonal()

                                                    },
                                                            on_error = { longToast("${it}")
                                                                updatePersonal()})


                                                }
                                        )


                                ).create().show()


                            }
                    )
            ).create().show()

        })


    }

    private fun setSchichtOpen(item: Schicht) {
        update_schicht(
                item, Schicht2Change(schicht = item, state = "open"),
                on_success = {
                    updatePersonal()
                    sendMessage(
                            to = "position${item.position.id}",
                            title = "${item.type.description} zu vergeben",
                            body = "${item.type.description} am ${item.day.format()}"
                    )
                    longToast("alle ${item.position.name} werden benachrichtigt")
                },
                on_error = {
                    longToast("${it}")

                    updatePersonal()
                }
        )


    }

    private fun setSchichtForTrade(item: Schicht) {


        update_schicht(item,
                Schicht2Change(schicht = item, state = "for_trade"),
                on_success = {
                    updatePersonal()
                    sendMessage(
                            to = "position${item.position.id}",
                            title = "${item.type.description} zum tauschen",
                            body = "${item.type.description} am ${item.day.format()}"
                    )
                    longToast("alle ${item.position.name} werden benachrichtigt")
                },
                on_error = {
                    longToast("${it}")
                    updatePersonal()
                }
        )


    }

    private fun setSchichtAccepted(item: Schicht) {


        update_schicht(item,
                Schicht2Change(item,
                        accept_id = 1),
                on_success = {
                    updatePersonal()
                },
                on_error = { longToast("$it")

                    updatePersonal()}
        )

    }

    private fun deleteSchicht(item: Schicht) {

        delete_schicht(item, { updatePersonal() },on_error = {
            toast(it)
        })
    }


}

package com.example.andenk.schichtln.gui

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.andenk.schichtln.R
import com.example.andenk.schichtln.helper.DBHelper
import com.example.andenk.schichtln.helper.createAlert
import com.example.andenk.schichtln.helper.format
import com.example.andenk.schichtln.helper.gson
import com.example.andenk.schichtln.line_number
import com.example.andenk.schichtln.modeladapter.OpenListAdapter
import com.example.andenk.schichtln.modeladapter.PersonalListAdapter
import com.example.andenk.schichtln.notification.sendMessage
import com.example.andenk.schichtln.openLoginDialog
import com.example.andenk.schichtln.pojos.Schicht
import com.example.andenk.schichtln.pojos.Schicht2Change
import com.example.andenk.schichtln.pojos.User
import com.example.andenk.schichtln.webservice.*
import kotlinx.android.synthetic.main.fragment_personal_open.view.*
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.support.v4.toast
import org.json.JSONArray


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [OpenFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [OpenFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OpenFragment : Fragment() {
    var convertview: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        convertview = inflater!!.inflate(R.layout.fragment_personal_open, container, false)

        convertview!!.swipe_container.setColorScheme(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark)

//        convertview!!.container_personal.visibility = (arguments.getInt("personalvisibility"))


        convertview!!.swipe_container.setOnRefreshListener({
            updateOpen()
        })


        updateOpen()



        return convertview
    }


    fun updateOpen() {

        convertview!!.swipe_container.isRefreshing = true

        var list: ArrayList<Schicht> = ArrayList()
        if (DBHelper(activity).getCurrentUser() != null) {
            get_open_schicht_by_user(DBHelper(activity).getCurrentUser()!!, on_success = {

                if (activity != null) {

                    convertview!!.list_personal_open.layoutManager = LinearLayoutManager(activity)
                    convertview!!.list_personal_open.adapter = OpenListAdapter(
                            context = activity,
                            objects = it as List<Schicht>,
                            set_taken = {
                                setSchichtTaken(it)
                            },
                            set_denied = {
                                setSchichtDenied(it)
                            },
                            offer_trade = {
                                offerSchichtForTrade(it)
                            },
                            update = {
                                updateOpen()
                            }
                    )

                }
                convertview!!.swipe_container.isRefreshing = false


            }, on_error = {
                println(it.message)
                longToast(it.localizedMessage)
                convertview!!.swipe_container.isRefreshing = false
                updateOpen()
            })

        } else {
            activity.startActivityForResult(Intent(activity, LoginDialog().javaClass), RESULTCODE_LOGIN)
        }
    }


    private fun offerSchichtForTrade(item: Schicht) {
        get_all_schichten_for_trade(DBHelper(context).getCurrentUser()!!, item.user!!, on_success = {
            val schichts_for_trade = it as MutableList<Schicht>
            val trade_offer_names = mutableListOf<Any>()
            for (i in it as MutableList<Schicht>) {

                trade_offer_names.add("${i.day.format("dd.MM.")} ${i.type.description}")
            }

            createAlert(
                    activity,
                    "für welche?",
                    trade_offer_names,
                    DialogInterface.OnClickListener({ _: DialogInterface, index: Int ->

                        update_schicht(item,
                                Schicht2Change(item, trade_for_id_id = schichts_for_trade[index].id),
                                on_success = {
                                    updateOpen()
                                    sendMessage(
                                            to = "${item.user!!.id}",
                                            title = "Tauschangebot für ${item.type.description}",
                                            body = "kanns du ${schichts_for_trade[index].type.description} am ${schichts_for_trade[index].day.format()} übernehmen"
                                    )
                                },
                                on_error = {
                                    toast(it)
                                    updateOpen()
                                }
                        )


                    })
            ).show()
        }, on_error = {
            longToast(it.localizedMessage)
            updateOpen()
        })
    }


    private fun setSchichtTaken(item: Schicht) {
        val db = DBHelper(activity)
        update_schicht(item,
                newschicht = Schicht2Change(
                        id = item.id,
                        accept = item.accept.id,
                        day = item.day,
                        note = item.note,
                        offered_to = null,
                        position = item.position.id,
                        type = item.type.id,
                        user = db.getCurrentUser()!!.id,
                        state = "closed",
                        trade_for_id = null

                ),
                on_success = {
                    updateOpen()
                    sendMessage(
                            "${item.user!!.id}",
                            "Du hast am ${item.day.format()} frei",
                            "${DBHelper(activity).getCurrentUser()!!.username} hat '${item.type.description}' übernommen"
                    )

                },
                on_error = {
                    longToast(it)
                    updateOpen()
                }
        )
    }

    private fun setSchichtDenied(item: Schicht) {
        val db = DBHelper(activity)
        update_schicht(item,
                newschicht = Schicht2Change(
                        id = item.id,
                        accept = item.accept.id,
                        day = item.day,
                        note = item.note,
                        offered_to = null,
                        position = item.position.id,
                        type = item.type.id,
                        user = item.user!!.id,
                        state = "closed",
                        trade_for_id = null

                ),
                on_success = {
                    updateOpen()
                    sendMessage(
                            "${item.user!!.id}",
                            "Dein Angebot wurde abgelehnt",
                            "${DBHelper(activity).getCurrentUser()!!.username} übernimmt '${item.type.description} - ${item.day.format()}' nicht"
                    )

                },
                on_error = {
                    longToast(it)
                    updateOpen()
                }
        )
    }


}

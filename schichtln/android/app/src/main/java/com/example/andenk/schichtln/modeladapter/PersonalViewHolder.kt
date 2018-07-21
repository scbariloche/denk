package com.example.andenk.schichtln.modeladapter

import android.content.Context
import android.content.DialogInterface
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.andenk.schichtln.helper.createAlert
import com.example.andenk.schichtln.format
import com.example.andenk.schichtln.notification.sendMessage
import com.example.andenk.schichtln.pojos.Schicht
import com.example.andenk.schichtln.pojos.Schicht2Change
import com.example.andenk.schichtln.teaser
import com.example.andenk.schichtln.webservice.get_schicht_by_id
import com.example.andenk.schichtln.webservice.update_schicht
import kotlinx.android.synthetic.main.display_user_list.view.*
import org.jetbrains.anko.longToast

/**
 * Created by andreas on 24.01.2018.
 */
class PersonalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(
            context: Context,
            item: Schicht,
            offer: (Schicht) -> Unit,
            setSchichtAccepted: (Schicht) -> Unit,
            deleteSchicht: (Schicht) -> Unit,
            setSchichtOpen: (Schicht) -> Unit,
            setSchichtForTrade: (Schicht) -> Unit,
            update: () -> Unit
    ) = with(itemView) {
        state_dot.visibility = View.VISIBLE


        state_dot.setImageResource(context.getResources().getIdentifier(item.state, "drawable", context.getPackageName()))


        if (item.trade_for_id != null) {
            state_dot.setImageResource(context.getResources().getIdentifier("for_offer_with_offer", "drawable", context.getPackageName()))
        }







        itemView.setOnClickListener({ view ->
            when (item.accept.id) {
                1 -> {
                    //CASE: someone made an offer for my schicht
                    if (item.state == "for_trade" && item.trade_for_id != null) {
                        get_schicht_by_id(item.trade_for_id!!.toLong(), {
                            val item2 = it
                            createAlert(
                                    context = context,
                                    message = "${item2.user!!.username} bietet ${item2.type.description} am ${item2.day.format("dd.MM.")}",
                                    yesOptionString = "annehmen",
                                    yesAction = DialogInterface.OnClickListener({ _: DialogInterface, _: Int ->
                                        update_schicht(item,
                                                Schicht2Change(
                                                        schicht = item,
                                                        user_id = item2.user!!.id,
                                                        state = "closed",
                                                        trade_for_id_id = null
                                                ),
                                                on_success = {
                                                    update_schicht(item,
                                                            Schicht2Change(
                                                                    schicht = item2,
                                                                    user_id = item.user!!.id,
                                                                    state = "closed",
                                                                    trade_for_id_id = null
                                                            ),
                                                            on_success = {
                                                                update()

                                                                sendMessage(
                                                                        "${item2.user!!.id}",
                                                                        "Der Tausch hat geklappt",
                                                                        "du arbeitest am ${item.day.format()} statt am ${item2.day.format()}"
                                                                )

                                                            },
                                                            on_error = {
                                                                context.longToast(it)
                                                                update()
                                                            }
                                                            ,
                                                            check_if_changed = false)
                                                },
                                                on_error = {
                                                    context.longToast(it)
                                                    update()
                                                })
                                    }), noOptionString = "ablehnen",
                                    noAction = DialogInterface.OnClickListener({ _: DialogInterface, _: Int ->
                                        update_schicht(item,
                                                Schicht2Change(
                                                        schicht = item,
                                                        user_id = item.user!!.id,
                                                        state = "for_trade",
                                                        trade_for_id_id = null
                                                ),
                                                on_success = {
                                                    update()
                                                    sendMessage(
                                                            "${item2.user!!.id}",
                                                            "${item.user!!.username} hat dein Tauschangebot abgelehnt",
                                                            "eventuell ist eine andere Schicht passender?"
                                                    )
                                                },
                                                on_error = {
                                                    context.longToast(it)
                                                    update()

                                                }
                                        )
                                    })).show()

                        }, {})


                    } else
//                    CASE: accepted and closed
                    {


                        val optionsList = mutableListOf<Any>("offen anbieten", "jemanden anbieten", "zum Tausch anbieten")
                        createAlert(context, "Du bist solange für die Schicht verantwortlich bis diese von einem anderen angenommen wird",
                                optionsList,
                                DialogInterface.OnClickListener(
                                        { _: DialogInterface,
                                          i_option: Int ->
                                            when (i_option) {
                                                0 -> setSchichtOpen(item)
                                                1 -> offer(item)
                                                2 -> setSchichtForTrade(item)
                                            }
                                        })).show()
                        if (item.offered_to != null) {
                            createAlert(context, "Du hast diese Schicht ${item.offered_to} angeboten")
                        }
                    }
                }
                2 -> {
                    createAlert(context, "Diese Schicht wurde geändert", DialogInterface.OnClickListener(
                            { _: DialogInterface,
                              _: Int ->
                                setSchichtAccepted(item)
                            })).show()
                }
                3 -> {
                    createAlert(context, "Diese Schicht wurde gestrichen", DialogInterface.OnClickListener(
                            { _: DialogInterface,
                              _: Int ->
                                deleteSchicht(item)
                            })).show()
                }
            }


        })

        display_userlist_tv2!!.text = "${item.type}"


        display_userlist_tv1!!.text = item.day.format("EE dd.MM.yyyy")
        if (item.note != null && item.note.length > 7) {
            display_userlist_tv3.text = item.note.teaser(
                    length = 7,
                    linebreak = true)
        } else {
            display_userlist_tv3.text = item.note
        }
        acc_dot.setImageResource(context.getResources().getIdentifier(
                item.accept.icon, "drawable", context.getPackageName()))
    }
}
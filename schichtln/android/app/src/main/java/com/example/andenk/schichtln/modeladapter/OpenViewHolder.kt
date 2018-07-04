package com.example.andenk.schichtln.modeladapter

import android.content.Context
import android.content.DialogInterface
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.andenk.schichtln.helper.DBHelper
import com.example.andenk.schichtln.helper.createAlert
import com.example.andenk.schichtln.helper.format
import com.example.andenk.schichtln.pojos.Schicht
import com.example.andenk.schichtln.pojos.Schicht2Change
import com.example.andenk.schichtln.teaser
import com.example.andenk.schichtln.webservice.update_schicht
import kotlinx.android.synthetic.main.display_user_list.view.*
import org.jetbrains.anko.longToast

/**
 * Created by andreas on 24.01.2018.
 */
class OpenViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(
            context: Context,
            item: Schicht,
            setSchichtTakenByUser: (Schicht) -> Unit,
            setSchichtDeniedByUser: (Schicht) -> Unit,
            offerTrade: (Schicht) -> Unit,
            update: () -> Unit
    ) = with(itemView) {
        state_dot.visibility = View.VISIBLE


        state_dot.setImageResource(context.getResources().getIdentifier(item.state, "drawable", context.getPackageName()))


        if (item.trade_for_id != null) {
            state_dot.setImageResource(context.getResources().getIdentifier("for_offer_with_offer", "drawable", context.getPackageName()))
        }

        itemView.setOnClickListener({ view ->
            if (item.user!!.id != DBHelper(context).getCurrentUser()!!.id) {


                when (item.state) {
                    "for_trade" -> {
                        if (item.trade_for_id == null) {
                            offerTrade(item)
                        } else {
                            context.longToast("es wurde hierfür bereits ein Angebot abgegeben")
                        }
                    }
                    "offered" -> {
                        createAlert(
                                context,
                                "möchtest du diese Schicht annehmen?", "ja",
                                DialogInterface.OnClickListener({ _: DialogInterface,
                                                                   _: Int ->
                                    setSchichtTakenByUser(item)
                                }), "nein", DialogInterface.OnClickListener({ _: DialogInterface, _: Int ->
                            setSchichtDeniedByUser(item)
                        })
                        ).show()
                    }
                    "open" -> {
                        createAlert(
                                context,
                                "möchtest du diese Schicht annehmen?",
                                DialogInterface.OnClickListener({ _: DialogInterface,
                                                                  _: Int ->
                                    setSchichtTakenByUser(item)
                                })
                        ).show()
                    }
                }


            } else {

                if (item.trade_for_id != null) {
                    createAlert(
                            context,
                            "Jemand hat einen Tausch angeboten. Lehne diesen ab, bevor du die Schicht zurücknimmst").show()
                } else {
                    createAlert(
                            context,
                            "möchtest du diese Schicht wieder zurücknehmen?",
                            DialogInterface.OnClickListener({ _: DialogInterface, _: Int ->
                                update_schicht(item,
                                        Schicht2Change(item, state = "closed", offered_to_id = null),
                                        on_success = { update() },
                                      on_error=  {
                                          context.longToast(it)
                                          update()
                                      }
                                )
                            })
                    ).show()
                }
            }
        })

        display_userlist_tv1!!.text = item.day.format("EE dd.MM.yyyy")

        display_userlist_tv2.text = "${item.type.description}  ${item.user!!.username}"

        if (item.note.length > 7) {
            display_userlist_tv3.text = item.note.teaser(
                    length = 7,
                    linebreak = true)
        } else {
            display_userlist_tv3.text = item.note
        }
        acc_dot!!.setImageResource(context.getResources().getIdentifier(item.accept.icon, "drawable", context.getPackageName()))

    }
}
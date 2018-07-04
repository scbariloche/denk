package com.example.andenk.schichtln.modeladapter

import android.content.Context
import android.content.DialogInterface
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.andenk.schichtln.helper.DBHelper
import com.example.andenk.schichtln.helper.createAlert
import com.example.andenk.schichtln.helper.format
import com.example.andenk.schichtln.notification.sendMessage
import com.example.andenk.schichtln.pojos.Schicht
import com.example.andenk.schichtln.pojos.Schicht2Change
import com.example.andenk.schichtln.webservice.update_schicht
import kotlinx.android.synthetic.main.display_user_list.view.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * Created by andreas on 24.01.2018.
 */
class DetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(
            context: Context,
            item: Schicht,
            updateList: () -> Unit
    ) = with(itemView) {


        display_userlist_tv1!!.text = "${item.user}"
        display_userlist_tv2!!.text = "${item.type}"


        itemView.onClick {

            if (DBHelper(context).getCurrentUser()!!.is_superuser) {
                if (item.accept.id != 3) {
                    build_delete_popup(context, itemView, item, { updateList() })
                } else {
                    build_activate_popup(context, itemView, item, { updateList() })
                }


            } else {
                createAlert(context, item.note)

            }
        }
        acc_dot!!.setImageResource(context.getResources().getIdentifier(
                item.accept.icon,
                "drawable",
                context.getPackageName()
        ));
    }

    fun build_activate_popup(activity: Context, retView: View, item: Schicht, update: () -> Unit) {

        createAlert(activity, "Diese Schicht wieder aktivieren?",

                DialogInterface.OnClickListener(
                        { _: DialogInterface,
                          i_option: Int ->

                            createAlert(
                                    activity,
                                    "${item.user} wird benachrichtigt und kann diese Änderung bestätigen",
                                    DialogInterface.OnClickListener(
                                            { d: DialogInterface, i: Int ->
                                                update_schicht(item,Schicht2Change(
                                                        schicht = item,
                                                        accept_id = 2
                                                ), on_success = {
                                                    activity.longToast("${item.user} wird benachrichtigt")
                                                    update()
                                                    sendMessage(
                                                            to="${item.user!!.id}",
                                                            title = "Du hast eine unbestätigte Änderung",
                                                            body = "Bitte bestätige, dass du am ${item.day.format()} arbeiten kannst"
                                                    )
                                                }, on_error = {
                                                    activity.longToast("${it}")
                                                    update()
                                                })
                                            }
                                    )
                            ).show()


                        })).show()


    }


    fun build_delete_popup(context: Context, retView: View, item: Schicht, update: () -> Unit) {
        createAlert(context, "Diese Schicht streichen?",

                DialogInterface.OnClickListener(
                        { _: DialogInterface,
                          i_option: Int ->

                            createAlert(
                                    context!!,
                                    "Diese Schicht wird als 'gestrichen' markiert und verschwindet sobald ${item.user} bestätigt",
                                    DialogInterface.OnClickListener(
                                            { d: DialogInterface, i: Int ->

                                                update_schicht(item,
                                                        Schicht2Change(item, accept_id = 3),
                                                        on_success = { context.longToast("${item.user?.username} wird benachrichtigt");update()
                                                            sendMessage(
                                                                    to="${item.user!!.id}",
                                                                    title = "Du hast am ${item.day.format()} frei",
                                                                    body = " Die Vertretung hat '${item.type.description}' gestrichen. Bitte bestätige dies"
                                                            )},
                                                        on_error = { context.longToast(it)
                                                            update() }
                                                )

                                            }
                                    )
                            ).show()


                        })).show()


    }
}


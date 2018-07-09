package com.example.andenk.schichtln.modeladapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.andenk.schichtln.format
import com.example.andenk.schichtln.pojos.Schicht
import com.example.andenk.schichtln.teaser
import kotlinx.android.synthetic.main.display_user_list.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * Created by andreas on 24.01.2018.
 */
class PositionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(
            context: Context,
            item: Schicht,
            date_visible: Int
    ) = with(itemView) {


        display_userlist_tv1!!.visibility = date_visible
        display_userlist_tv1!!.text = item.day.format("EE dd.MM.yyyy")

        if (item.note.length > 9) {
            display_userlist_tv3.text = item.note.teaser(
                    length = 9,
                    linebreak = true
            )
        } else {
            display_userlist_tv3.text = item.note
        }

        display_userlist_tv3.text = item.user?.username
        display_userlist_tv2.text = item.type.description
        acc_dot!!.setImageResource(context.getResources().getIdentifier(item.accept.icon, "drawable", context.getPackageName()));

    }


}
package com.example.andenk.schichtln.modeladapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.example.andenk.schichtln.R
import com.example.andenk.schichtln.inflate
import com.example.andenk.schichtln.pojos.Schicht

/**
 * Created by andreas on 24.01.2018.
 */
class PositionListAdapter(
        val context: Context,
        val objects: List<Schicht>
) : RecyclerView.Adapter<PositionViewHolder>() {


    override fun onBindViewHolder(holder: PositionViewHolder, position: Int)
            = holder.bind(
            context = context,
            item = objects[position],
            date_visible = if (position > 0) (
                    if (objects[position - 1].day == objects[position].day) View.INVISIBLE
                    else View.VISIBLE)
            else View.VISIBLE)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PositionViewHolder {
        return PositionViewHolder(itemView = parent.inflate(R.layout.display_user_list))
    }

    override fun getItemCount(): Int = objects.size
}

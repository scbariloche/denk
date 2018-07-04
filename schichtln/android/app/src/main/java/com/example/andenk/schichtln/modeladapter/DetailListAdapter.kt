package com.example.andenk.schichtln.modeladapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.example.andenk.schichtln.R
import com.example.andenk.schichtln.inflate
import com.example.andenk.schichtln.pojos.Schicht

/**
 * Created by andreas on 24.01.2018.
 */
class DetailListAdapter(
        val context: Context,
        val objects: List<Schicht>,
        val update: () -> Unit
) : RecyclerView.Adapter<DetailViewHolder>() {


    override fun onBindViewHolder(holder: DetailViewHolder, position: Int)
            = holder.bind(
            context = context,
            item = objects[position],
            updateList = update)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        return DetailViewHolder(itemView = parent.inflate(R.layout.display_user_list))
    }

    override fun getItemCount(): Int = objects.size
}

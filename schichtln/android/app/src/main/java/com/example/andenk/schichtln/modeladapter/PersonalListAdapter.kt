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
class PersonalListAdapter(
        val context: Context,
        val objects: List<Schicht>,
        val offer: (Schicht) -> Unit,
        val set_accepted: (Schicht) -> Unit,
        val delete: (Schicht) -> Unit,
        val set_open: (Schicht) -> Unit,
        val set_for_trade: (Schicht) -> Unit,
        val update: () -> Unit
) : RecyclerView.Adapter<PersonalViewHolder>() {


    override fun onBindViewHolder(holder: PersonalViewHolder, position: Int) = holder.bind(
            context = context,
            item = objects[position],
            offer = offer,
            setSchichtAccepted = set_accepted,
            deleteSchicht = delete,
            setSchichtOpen = set_open,
            setSchichtForTrade = set_for_trade,
            update = update
    )


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonalViewHolder {
        return PersonalViewHolder(itemView = parent.inflate(R.layout.display_user_list))
    }

    override fun getItemCount(): Int = objects.size
}

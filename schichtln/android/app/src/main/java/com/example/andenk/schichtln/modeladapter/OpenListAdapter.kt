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
class OpenListAdapter(
        val context: Context,
        val objects: List<Schicht>,
        val set_taken: (Schicht) -> Unit,
        val set_denied: (Schicht) -> Unit,
        val offer_trade: (Schicht) -> Unit,
        val update: () -> Unit
) : RecyclerView.Adapter<OpenViewHolder>() {


    override fun onBindViewHolder(holder: OpenViewHolder, position: Int) = holder.bind(
            context = context,
            item = objects[position],
            setSchichtTakenByUser = set_taken,
            setSchichtDeniedByUser = set_denied,
            offerTrade = offer_trade,
            update = update
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OpenViewHolder {
        return OpenViewHolder(itemView = parent.inflate(R.layout.display_user_list))
    }

    override fun getItemCount(): Int = objects.size
}

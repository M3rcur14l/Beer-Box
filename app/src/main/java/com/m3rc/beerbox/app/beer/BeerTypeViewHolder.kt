package com.m3rc.beerbox.app.beer

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.m3rc.beerbox.R
import com.m3rc.beerbox.kx.BeerType

class BeerTypeViewHolder(parent: ViewGroup, private val onClick: (BeerType) -> (Unit)) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.beer_type_button, parent, false)
) {
    private val beerType = itemView.findViewById<TextView>(R.id.beerType)

    fun bind(beerTypes: BeerType) {
        beerType.text = beerTypes.name
        beerType.setOnClickListener { onClick.invoke(beerTypes) }
    }
}
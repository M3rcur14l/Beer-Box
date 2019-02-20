package com.m3rc.beerbox.app.beer

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.m3rc.beerbox.R
import com.m3rc.beerbox.kx.BeerType

class BeerTypeViewHolder(parent: ViewGroup, private val onClick: (Pair<BeerType, Boolean>) -> (Unit)) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.beer_type_button, parent, false)
    ) {
    private val beerType = itemView.findViewById<ToggleButton>(R.id.beerType)

    fun bind(beerTypes: Pair<BeerType, Boolean>) {
        beerType.text = beerTypes.first.displayName
        beerType.textOn = beerTypes.first.displayName
        beerType.textOff = beerTypes.first.displayName
        beerType.setOnClickListener { onClick.invoke(Pair(beerTypes.first, !beerTypes.second)) }
        beerType.isChecked = beerTypes.second
    }
}
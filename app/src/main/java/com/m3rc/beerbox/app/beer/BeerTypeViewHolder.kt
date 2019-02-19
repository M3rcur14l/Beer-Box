package com.m3rc.beerbox.app.beer

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.m3rc.beerbox.R
import com.m3rc.beerbox.kx.BeerType

class BeerTypeViewHolder(parent: ViewGroup, private val onClick: (BeerType) -> (Unit)) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.beer_type_button, parent, false)
) {
    private val beerType = itemView.findViewById<ToggleButton>(R.id.beerType)

    fun bind(beerTypes: BeerType, selectedBeerType: BeerType?) {
        beerType.text = beerTypes.name
        beerType.textOn = beerTypes.name
        beerType.textOff = beerTypes.name
        beerType.setOnClickListener { onClick.invoke(beerTypes) }
        beerType.isChecked = selectedBeerType?.equals(beerTypes) ?: false
    }
}
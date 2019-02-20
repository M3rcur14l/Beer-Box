package com.m3rc.beerbox.app.beer

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.m3rc.beerbox.R
import com.m3rc.beerbox.data.Beer

class BeerViewHolder(parent: ViewGroup, private val onClick: (Beer) -> (Unit)) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.row_beer, parent, false)
) {
    private val image = itemView.findViewById<ImageView>(R.id.beerImage)
    private val name = itemView.findViewById<TextView>(R.id.beerName)
    private val tagLine = itemView.findViewById<TextView>(R.id.beerTagLine)
    private val description = itemView.findViewById<TextView>(R.id.beerDescription)

    fun bind(beer: Beer?) {
        beer?.let {
            Glide.with(image)
                .load(beer.image)
                .transition(withCrossFade())
                .apply(glideOptions)
                .into(image)
            name.text = beer.name
            tagLine.text = beer.tagLine
            description.text = beer.description
            itemView.setOnClickListener { onClick.invoke(beer) }
        }

    }

    companion object {
        val glideOptions = RequestOptions().placeholder(R.drawable.beer)
    }
}
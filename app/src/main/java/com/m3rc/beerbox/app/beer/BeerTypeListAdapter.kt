package com.m3rc.beerbox.app.beer

import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.m3rc.beerbox.kx.BeerType

class BeerTypeListAdapter : ListAdapter<BeerType, BeerTypeViewHolder>(diffCallback) {

    var selectedBeerType: BeerType? = null
    val beerTypeClick = MutableLiveData<BeerType>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BeerTypeViewHolder(parent) {
        beerTypeClick.value = it
    }

    override fun onBindViewHolder(holder: BeerTypeViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, selectedBeerType) }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<BeerType>() {
            override fun areItemsTheSame(oldItem: BeerType, newItem: BeerType) =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: BeerType, newItem: BeerType) =
                oldItem.name == newItem.name
        }
    }

}
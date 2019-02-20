package com.m3rc.beerbox.app.beer

import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.m3rc.beerbox.kx.BeerType

class BeerTypeListAdapter : ListAdapter<Pair<BeerType, Boolean>, BeerTypeViewHolder>(diffCallback) {

    val beerTypeClick = MutableLiveData<Pair<BeerType, Boolean>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BeerTypeViewHolder(parent) {
        beerTypeClick.value = it
    }

    override fun onBindViewHolder(holder: BeerTypeViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Pair<BeerType, Boolean>>() {
            override fun areItemsTheSame(oldItem: Pair<BeerType, Boolean>, newItem: Pair<BeerType, Boolean>) =
                oldItem.first.name == newItem.first.name

            override fun areContentsTheSame(oldItem: Pair<BeerType, Boolean>, newItem: Pair<BeerType, Boolean>) =
                oldItem == newItem
        }
    }

}
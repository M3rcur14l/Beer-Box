package com.m3rc.beerbox.app.beer

import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.m3rc.beerbox.kx.BeerType

class BeerTypeListAdapter : ListAdapter<Array<BeerType>, BeerTypeViewHolder>(diffCallback) {

    val beerTypeClick = MutableLiveData<Array<BeerType>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BeerTypeViewHolder(parent) {
        beerTypeClick.value = it
    }

    override fun onBindViewHolder(holder: BeerTypeViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Array<BeerType>>() {
            override fun areItemsTheSame(oldItem: Array<BeerType>, newItem: Array<BeerType>) =
                oldItem.contentDeepEquals(newItem)

            override fun areContentsTheSame(oldItem: Array<BeerType>, newItem: Array<BeerType>) =
                oldItem.contentDeepEquals(newItem)
        }
    }

}
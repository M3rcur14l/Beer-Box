package com.m3rc.beerbox.app.beer

import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.m3rc.beerbox.data.Beer

class BeerListAdapter : PagedListAdapter<Beer, BeerViewHolder>(diffCallback) {

    val beerClick = MutableLiveData<Beer>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BeerViewHolder(parent) {
        beerClick.value = it
    }

    override fun onBindViewHolder(holder: BeerViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Beer>() {
            override fun areItemsTheSame(oldItem: Beer, newItem: Beer): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Beer, newItem: Beer): Boolean =
                oldItem == newItem
        }
    }
}
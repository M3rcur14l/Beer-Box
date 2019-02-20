package com.m3rc.beerbox.app.beer

import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.m3rc.beerbox.R
import com.m3rc.beerbox.bus.annotation.ExecutionState.RUNNING
import com.m3rc.beerbox.bus.state.LoadingState
import com.m3rc.beerbox.data.Beer

class BeerListAdapter : PagedListAdapter<Beer, RecyclerView.ViewHolder>(diffCallback) {

    val beerClick = MutableLiveData<Beer>()
    var loadingState: LoadingState? = null
        set(newState) {
            val previousState = loadingState
            val hadExtraRow = hasExtraRow()
            field = newState
            val hasExtraRow = hasExtraRow()
            if (hadExtraRow != hasExtraRow) {
                if (hadExtraRow) {
                    notifyItemRemoved(super.getItemCount())
                } else {
                    notifyItemInserted(super.getItemCount())
                }
            } else if (hasExtraRow && previousState != newState) {
                notifyItemChanged(itemCount - 1)
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.row_beer -> BeerViewHolder(parent) { beerClick.value = it }
            R.layout.row_progress -> ProgressViewHolder(parent)
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.row_beer -> (holder as BeerViewHolder).bind(getItem(position))
            R.layout.row_progress -> (holder as ProgressViewHolder).bind(loadingState)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1)
            R.layout.row_progress
        else
            R.layout.row_beer
    }

    override fun getItemCount() = super.getItemCount() + if (hasExtraRow()) 1 else 0

    private fun hasExtraRow() = loadingState?.state == RUNNING

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Beer>() {
            override fun areItemsTheSame(oldItem: Beer, newItem: Beer): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Beer, newItem: Beer): Boolean =
                oldItem == newItem
        }
    }
}
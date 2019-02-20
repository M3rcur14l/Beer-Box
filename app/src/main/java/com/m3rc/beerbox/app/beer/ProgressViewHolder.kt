package com.m3rc.beerbox.app.beer

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.m3rc.beerbox.R
import com.m3rc.beerbox.bus.annotation.ExecutionState.RUNNING
import com.m3rc.beerbox.bus.state.LoadingState

class ProgressViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.row_progress, parent, false)
) {
    private val progress = itemView.findViewById<View>(R.id.progress)

    fun bind(loadingState: LoadingState?) {
        progress.visibility = if (loadingState?.state == RUNNING) VISIBLE else GONE
    }

}
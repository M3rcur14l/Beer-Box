package com.m3rc.beerbox.app.beer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.m3rc.beerbox.R
import com.m3rc.beerbox.app.DaggerFragment
import com.m3rc.beerbox.data.Beer
import com.m3rc.beerbox.kx.viewModel
import kotlinx.android.synthetic.main.fragment_beer.*
import javax.inject.Inject

class BeerFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: BeerViewModel

    companion object {
        fun newInstance() = BeerFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_beer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.let { context ->
            viewModel = viewModel(viewModelFactory)
            val adapter = BeerListAdapter()

            beerList.layoutManager = LinearLayoutManager(context)
            beerList.adapter = adapter
            viewModel.beerList.observe(this, Observer<PagedList<Beer>> {
                adapter.submitList(it)
            })
            adapter.beerClick.observe(this, Observer<Beer> {
                //TODO show beer details view
            })
        }

    }
}
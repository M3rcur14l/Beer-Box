package com.m3rc.beerbox.app.beer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.DividerItemDecoration
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
    private lateinit var viewModel: BeerViewModel

    companion object {
        fun newInstance() = BeerFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_beer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = viewModel(viewModelFactory)
        val adapter = BeerListAdapter()
        context?.let { context ->
            beerList.layoutManager = LinearLayoutManager(context)
            val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            divider.setDrawable(resources.getDrawable(R.drawable.list_divider, null))
            beerList.addItemDecoration(divider)
            beerList.adapter = adapter

        }
        viewModel.beerList.observe(this, Observer<PagedList<Beer>> {
            adapter.submitList(it)
        })
        adapter.beerClick.observe(this, Observer<Beer> {
            BeerDetailsBottomDialog.newInstance(it).show(childFragmentManager, "Beer Details")
        })

    }
}
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
import com.m3rc.beerbox.app.beer.adapter.BeerListAdapter
import com.m3rc.beerbox.app.beer.adapter.BeerTypeListAdapter
import com.m3rc.beerbox.bus.Bus
import com.m3rc.beerbox.bus.state.LoadingState
import com.m3rc.beerbox.data.Beer
import com.m3rc.beerbox.kx.BeerType
import com.m3rc.beerbox.kx.bindToLifecycle
import com.m3rc.beerbox.kx.range
import com.m3rc.beerbox.kx.viewModel
import kotlinx.android.synthetic.main.fragment_beer.*
import javax.inject.Inject

class BeerFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: BeerViewModel
    lateinit private var beerTypeAdapter: BeerTypeListAdapter

    companion object {
        fun newInstance() = BeerFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_beer, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = viewModel(viewModelFactory)
        val beerAdapter = BeerListAdapter()
        beerTypeAdapter = BeerTypeListAdapter()
        context?.let { context ->
            //Beer adapter setup
            beerList.setHasFixedSize(true)
            beerList.layoutManager = LinearLayoutManager(context)
            val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            divider.setDrawable(resources.getDrawable(R.drawable.list_divider, null))
            beerList.addItemDecoration(divider)
            beerList.adapter = beerAdapter

            //Beer type adapter setup
            beerTypes.setHasFixedSize(true)
            beerTypes.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            beerTypes.adapter = beerTypeAdapter
            refreshBeerTypes()
        }
        viewModel.beerList.observe(this, Observer<PagedList<Beer>> {
            beerAdapter.submitList(it)
        })
        beerAdapter.beerClick.observe(this, Observer<Beer> {
            BeerDetailsBottomDialog.newInstance(it).show(childFragmentManager, "Beer Details")
        })
        beerTypeAdapter.beerTypeClick.observe(this, Observer<Pair<BeerType, Boolean>> { type ->
            refreshBeerTypes(type)
            viewModel.refreshList(ebcRange = if (type.second) type.first.range() else null)
        })

        Bus.get().subscribeToState(LoadingState::class.java) { beerAdapter.loadingState = it }
            .bindToLifecycle(this)
    }

    private fun refreshBeerTypes(selectedType: Pair<BeerType, Boolean>? = null) {
        beerTypeAdapter.submitList(BeerType.values()
            .toList()
            .filter { it != BeerType.UNKNOWN }
            .map { Pair(it, it == selectedType?.first) })
    }

    fun onBackPressed(): Boolean {
        return if (viewModel.beerNameFilter != null || viewModel.ebcRange != null) {
            refreshBeerTypes()
            viewModel.refreshList()
            true
        } else
            false
    }


}
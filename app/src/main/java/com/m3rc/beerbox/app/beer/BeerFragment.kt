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
import com.m3rc.beerbox.bus.Bus
import com.m3rc.beerbox.bus.event.NewBeerPageEvent
import com.m3rc.beerbox.data.Beer
import com.m3rc.beerbox.kx.*
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

        viewModel = viewModel(viewModelFactory)
        val beerAdapter = BeerListAdapter()
        val beerTypeAdapter = BeerTypeListAdapter()
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

            Bus.get().subscribeToEvent(NewBeerPageEvent::class.java) { e ->
                if (beerTypeAdapter.selectedBeerType == null) {
                    val typesSet = e.beerList
                        .map { it.type() }
                        .filter { it != BeerType.UNKNOWN }
                        .toSortedSet()
                    beerTypeAdapter.submitList(typesSet.toList())
                }
            }.bindToLifecycle(this)
        }
        viewModel.beerList.observe(this, Observer<PagedList<Beer>> {
            beerAdapter.submitList(it)
        })
        beerAdapter.beerClick.observe(this, Observer<Beer> {
            BeerDetailsBottomDialog.newInstance(it).show(childFragmentManager, "Beer Details")
        })
        beerTypeAdapter.beerTypeClick.observe(this, Observer<BeerType> {
            if (it == beerTypeAdapter.selectedBeerType) {
                beerTypeAdapter.selectedBeerType = null
                viewModel.dataSourceFactory.ebcRange = null
            } else {
                beerTypeAdapter.selectedBeerType = it
                beerTypeAdapter.submitList(listOf(it))
                viewModel.dataSourceFactory.ebcRange = it.range()
            }
            viewModel.dataSourceFactory.dataSource.invalidate()
        })

    }
}
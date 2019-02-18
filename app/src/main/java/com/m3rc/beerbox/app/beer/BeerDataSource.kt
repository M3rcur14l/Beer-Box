package com.m3rc.beerbox.app.beer

import androidx.paging.PositionalDataSource
import com.m3rc.beerbox.app.LifecycleViewModel
import com.m3rc.beerbox.data.Beer
import com.m3rc.beerbox.data.PunkService
import com.m3rc.beerbox.kx.bindToLifecycle
import javax.inject.Inject

class BeerDataSource constructor(
    private val service: PunkService,
    private val viewModel: LifecycleViewModel
) : PositionalDataSource<Beer>() {

    private var pageSize: Int = 0

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Beer>) {
        service.getBeers(page = (params.startPosition / pageSize) + 1, perPage = params.loadSize)
            .subscribe(
                { list -> callback.onResult(list) }, {
                    //TODO
                })
            .bindToLifecycle(viewModel)
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Beer>) {
        pageSize = params.pageSize
        service.getBeers(page = 1, perPage = params.pageSize)
            .subscribe(
                { list -> callback.onResult(list, 1) }, {
                    //TODO
                })
            .bindToLifecycle(viewModel)
    }
}
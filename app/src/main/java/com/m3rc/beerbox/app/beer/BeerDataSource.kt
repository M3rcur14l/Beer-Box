package com.m3rc.beerbox.app.beer

import androidx.lifecycle.LifecycleOwner
import androidx.paging.PositionalDataSource
import com.m3rc.beerbox.data.Beer
import com.m3rc.beerbox.data.PunkService
import com.m3rc.beerbox.kx.bindToLifecycle

class BeerDataSource constructor(
    private val service: PunkService,
    private val lifecycleOwner: LifecycleOwner
) : PositionalDataSource<Beer>() {

    private var pageSize: Int = 0

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Beer>) {
        service.getBeers(page = (params.startPosition / pageSize) + 1, perPage = params.loadSize)
            .subscribe(
                { list -> callback.onResult(list) }, {
                    //TODO error
                })
            .bindToLifecycle(lifecycleOwner)
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Beer>) {
        pageSize = params.pageSize
        service.getBeers(page = 1, perPage = params.pageSize)
            .subscribe(
                { list -> callback.onResult(list, 1) }, {
                    //TODO error
                })
            .bindToLifecycle(lifecycleOwner)
    }
}
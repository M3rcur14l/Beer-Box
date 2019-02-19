package com.m3rc.beerbox.app.beer

import androidx.lifecycle.LifecycleOwner
import androidx.paging.PositionalDataSource
import com.m3rc.beerbox.bus.Bus
import com.m3rc.beerbox.bus.annotation.ExecutionState.COMPLETED
import com.m3rc.beerbox.bus.annotation.ExecutionState.FAILED
import com.m3rc.beerbox.bus.event.NewBeerPageEvent
import com.m3rc.beerbox.bus.state.LoadingState
import com.m3rc.beerbox.data.Beer
import com.m3rc.beerbox.data.PunkService
import com.m3rc.beerbox.kx.bindToLifecycle

class BeerDataSource constructor(
    private val service: PunkService,
    private val beerNameFilter: String? = null,
    private val lifecycleOwner: LifecycleOwner
) : PositionalDataSource<Beer>() {

    private var pageSize: Int = 0

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Beer>) {
        service.getBeers(
            page = (params.startPosition / pageSize) + 1,
            perPage = params.loadSize,
            beerName = beerNameFilter
        )
            .subscribe(
                { list ->
                    callback.onResult(list)
                    Bus.get().postEvent(NewBeerPageEvent(list))
                    Bus.get().postState(LoadingState(COMPLETED))
                }, {
                    Bus.get().postState(LoadingState(FAILED))
                })
            .bindToLifecycle(lifecycleOwner)
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Beer>) {
        pageSize = params.pageSize
        service.getBeers(
            page = 1,
            perPage = params.pageSize,
            beerName = beerNameFilter
        )
            .subscribe(
                { list ->
                    callback.onResult(list, 1)
                    Bus.get().postEvent(NewBeerPageEvent(list))
                    Bus.get().postState(LoadingState(COMPLETED))
                }, {
                    Bus.get().postState(LoadingState(FAILED))
                })
            .bindToLifecycle(lifecycleOwner)
    }
}
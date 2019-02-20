package com.m3rc.beerbox.app.beer

import androidx.lifecycle.LifecycleOwner
import androidx.paging.PositionalDataSource
import com.m3rc.beerbox.bus.Bus
import com.m3rc.beerbox.bus.annotation.ExecutionState.*
import com.m3rc.beerbox.bus.event.NewBeerPageEvent
import com.m3rc.beerbox.bus.state.LoadingState
import com.m3rc.beerbox.data.Beer
import com.m3rc.beerbox.data.PunkService
import com.m3rc.beerbox.kx.bindToLifecycle

class BeerDataSource(
    private val service: PunkService,
    private val beerNameFilter: String? = null,
    private val ebcRange: ClosedFloatingPointRange<Float>? = null,
    private val lifecycleOwner: LifecycleOwner
) : PositionalDataSource<Beer>() {

    private var pageSize: Int = 0
    private val beerIdSet = mutableSetOf<Long>()

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Beer>) {
        Bus.get().postState(LoadingState(RUNNING))
        service.getBeers(
            page = (params.startPosition / pageSize) + 1,
            perPage = params.loadSize,
            beerName = beerNameFilter,
            ebcLowerBound = ebcRange?.start?.toInt(),
            ebcUpperBound = ebcRange?.endInclusive?.toInt()
        )
            .subscribe(
                { list ->
                    callback.onResult(computeList(list))
                    if (!list.isEmpty()) Bus.get().postEvent(NewBeerPageEvent(list))
                    Bus.get().postState(LoadingState(COMPLETED))
                }, {
                    Bus.get().postState(LoadingState(FAILED))
                })
            .bindToLifecycle(lifecycleOwner)
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Beer>) {
        Bus.get().postState(LoadingState(RUNNING_INITIAL))
        pageSize = params.pageSize
        service.getBeers(
            page = 1,
            perPage = params.pageSize,
            beerName = beerNameFilter,
            ebcLowerBound = ebcRange?.start?.toInt(),
            ebcUpperBound = ebcRange?.endInclusive?.toInt()
        )
            .subscribe(
                { list ->
                    callback.onResult(computeList(list), 0)
                    if (!list.isEmpty()) Bus.get().postEvent(NewBeerPageEvent(list))
                    Bus.get().postState(LoadingState(COMPLETED))
                }, {
                    Bus.get().postState(LoadingState(FAILED))
                })
            .bindToLifecycle(lifecycleOwner)
    }

    private fun computeList(list: List<Beer>) =
        list.filter { !beerIdSet.contains(it.id) }
            .map {
                it.id?.let { id -> beerIdSet.add(id) }
                it
            }
}
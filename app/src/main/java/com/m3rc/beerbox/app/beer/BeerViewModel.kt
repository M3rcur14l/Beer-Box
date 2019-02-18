package com.m3rc.beerbox.app.beer

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.m3rc.beerbox.app.LifecycleViewModel
import com.m3rc.beerbox.data.Beer
import javax.inject.Inject

class BeerViewModel @Inject constructor(dataSourceFactory: BeerDataSourceFactory) : LifecycleViewModel() {

    private val config = PagedList.Config.Builder()
        .setEnablePlaceholders(true)
        .setInitialLoadSizeHint(PAGE_SIZE)
        .setPageSize(PAGE_SIZE)
        .build()

    val beerList = RxPagedListBuilder<Int, Beer>(dataSourceFactory, config).buildObservable()

    companion object {
        const val PAGE_SIZE = 20
    }
}
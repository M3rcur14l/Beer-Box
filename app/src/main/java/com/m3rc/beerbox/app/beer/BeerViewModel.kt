package com.m3rc.beerbox.app.beer

import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.m3rc.beerbox.app.LifecycleViewModel
import com.m3rc.beerbox.data.Beer
import javax.inject.Inject

class BeerViewModel @Inject constructor(dataSourceFactory: BeerDataSourceFactory) : LifecycleViewModel() {

    private val config = PagedList.Config.Builder()
        .setEnablePlaceholders(false) //The API doesn't return the total result count =(
        .setInitialLoadSizeHint(PAGE_SIZE)
        .setPageSize(PAGE_SIZE)
        .build()

    val beerList = LivePagedListBuilder<Int, Beer>(
        dataSourceFactory.apply { lifecycleOwner = this@BeerViewModel },
        config
    ).build()

    companion object {
        const val PAGE_SIZE = 20
    }
}
package com.m3rc.beerbox.app.beer.data

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.m3rc.beerbox.data.Beer
import javax.inject.Inject

private const val PAGE_SIZE = 25

class BeerRepository @Inject constructor(private val remoteDataSourceFactory: BeerRemoteDataSourceFactory) {

    private val config = PagedList.Config.Builder()
        .setEnablePlaceholders(false) //The API doesn't return the total result count =(
        .setInitialLoadSizeHint(PAGE_SIZE)
        .setPageSize(PAGE_SIZE)
        .build()
    var beerNameFilter: String?
        get() = remoteDataSourceFactory.beerNameFilter
        set(value) {
            remoteDataSourceFactory.beerNameFilter = value
        }
    var ebcRange: ClosedFloatingPointRange<Float>?
        get() = remoteDataSourceFactory.ebcRange
        set(value) {
            remoteDataSourceFactory.ebcRange = value
        }

    fun getPagedList(lifecycleOwner: LifecycleOwner): LiveData<PagedList<Beer>> {
        return LivePagedListBuilder<Int, Beer>(
            remoteDataSourceFactory.apply { this.lifecycleOwner = lifecycleOwner },
            config
        ).build()
    }

    fun invalidate() = remoteDataSourceFactory.remoteDataSource.invalidate()

}
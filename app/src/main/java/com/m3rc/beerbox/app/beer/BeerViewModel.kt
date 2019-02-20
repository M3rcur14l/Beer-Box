package com.m3rc.beerbox.app.beer

import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.m3rc.beerbox.app.LifecycleViewModel
import com.m3rc.beerbox.data.Beer
import com.m3rc.beerbox.data.PunkService
import com.m3rc.beerbox.widget.searchview.Suggestion
import io.reactivex.Single
import javax.inject.Inject

class BeerViewModel @Inject constructor(
    private val dataSourceFactory: BeerDataSourceFactory,
    private val service: PunkService
) : LifecycleViewModel() {

    private val config = PagedList.Config.Builder()
        .setEnablePlaceholders(false) //The API doesn't return the total result count =(
        .setInitialLoadSizeHint(PAGE_SIZE)
        .setPageSize(PAGE_SIZE)
        .build()
    val beerList = LivePagedListBuilder<Int, Beer>(
        dataSourceFactory.apply { lifecycleOwner = this@BeerViewModel },
        config
    ).build()
    var beerNameFilter: String?
        get() = dataSourceFactory.beerNameFilter
        set(value) {
            dataSourceFactory.beerNameFilter = value
        }
    var ebcRange: ClosedFloatingPointRange<Float>?
        get() = dataSourceFactory.ebcRange
        set(value) {
            dataSourceFactory.ebcRange = value
        }

    fun getSuggestion(input: String): Single<List<Suggestion>> =
        service.getBeers(beerName = input, perPage = SUGGESTIONS)
            .toObservable()
            .flatMapIterable { it }
            .map { Suggestion(0, it.name ?: "") }
            .toList()

    fun refreshList() {
        dataSourceFactory.dataSource.invalidate()
    }

    companion object {
        private const val PAGE_SIZE = 10
        private const val SUGGESTIONS = 5
    }
}
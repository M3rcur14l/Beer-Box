package com.m3rc.beerbox.app.beer

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.m3rc.beerbox.data.Beer
import com.m3rc.beerbox.data.PunkService
import javax.inject.Inject

class BeerDataSourceFactory @Inject constructor(
    private val service: PunkService
) : DataSource.Factory<Int, Beer>() {

    lateinit var dataSource: BeerDataSource
    lateinit var lifecycleOwner: LifecycleOwner
    var beerNameFilter: String? = null

    override fun create() = BeerDataSource(service, beerNameFilter, lifecycleOwner).also { dataSource = it }

}
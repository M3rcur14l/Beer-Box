package com.m3rc.beerbox.app.beer.data

import androidx.lifecycle.LifecycleOwner
import androidx.paging.DataSource
import com.m3rc.beerbox.data.Beer
import com.m3rc.beerbox.data.PunkService
import javax.inject.Inject

class BeerRemoteDataSourceFactory @Inject constructor(
    private val service: PunkService
) : DataSource.Factory<Int, Beer>() {

    lateinit var remoteDataSource: BeerRemoteDataSource
    lateinit var lifecycleOwner: LifecycleOwner
    var beerNameFilter: String? = null
    var ebcRange: ClosedFloatingPointRange<Float>? = null

    override fun create() = BeerRemoteDataSource(
        service,
        beerNameFilter,
        ebcRange,
        lifecycleOwner
    ).also { remoteDataSource = it }

}
package com.m3rc.beerbox.app.beer

import androidx.paging.DataSource
import com.m3rc.beerbox.app.LifecycleViewModel
import com.m3rc.beerbox.data.Beer
import com.m3rc.beerbox.data.PunkService
import javax.inject.Inject

class BeerDataSourceFactory @Inject constructor(
    private val service: PunkService,
    private val viewModel: LifecycleViewModel
) : DataSource.Factory<Int, Beer>() {

    override fun create() = BeerDataSource(service, viewModel)

}
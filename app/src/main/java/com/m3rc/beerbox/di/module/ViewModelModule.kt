package com.m3rc.beerbox.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.m3rc.beerbox.app.beer.BeerViewModel
import com.m3rc.beerbox.di.provider.ViewModelFactory
import com.m3rc.beerbox.di.scope.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(BeerViewModel::class)
    abstract fun bindsBeerViewModel(beerViewModel: BeerViewModel): ViewModel

}
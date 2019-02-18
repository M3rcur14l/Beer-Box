package com.m3rc.beerbox.di.module

import androidx.lifecycle.ViewModelProvider
import com.m3rc.beerbox.di.provider.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    /*@Binds
    @IntoMap
    @ViewModelKey(ViewModel:class)
    abstract fun bindsMoviesViewModel(moviesViewModel: ViewModel): ViewModel*/
}
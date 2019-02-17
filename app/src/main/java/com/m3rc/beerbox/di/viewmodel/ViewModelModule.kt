package com.m3rc.beerbox.di.viewmodel

import androidx.lifecycle.ViewModelProvider
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
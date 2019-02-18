package com.m3rc.beerbox.di.module.fragment

import com.m3rc.beerbox.app.beer.BeerFragment
import com.m3rc.beerbox.di.scope.FragmentScoped
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BeerFragmentModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun beerFragment(): BeerFragment

}

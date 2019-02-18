package com.m3rc.beerbox.di.module

import com.m3rc.beerbox.app.beer.BeerActivity
import com.m3rc.beerbox.di.module.fragment.BeerFragmentModule
import com.m3rc.beerbox.di.scope.ActivityScoped
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [BeerFragmentModule::class])
    abstract fun mainActivity(): BeerActivity

}

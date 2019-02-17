package com.m3rc.beerbox.di.module

import com.m3rc.beerbox.MainActivity
import com.m3rc.beerbox.di.scope.ActivityScoped
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun mainActivity(): MainActivity

}

package com.m3rc.beerbox.di.module

import android.content.Context
import com.m3rc.beerbox.Application
import dagger.Binds
import dagger.Module

@Module
abstract class ApplicationModule {

    @Binds
    abstract fun bindContext(application: Application): Context

}
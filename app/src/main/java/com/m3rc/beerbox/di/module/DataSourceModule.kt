package com.m3rc.beerbox.di.module

import com.m3rc.beerbox.data.PunkService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class DataSourceModule {

    @Singleton
    @Provides
    internal fun providesPunkService(retrofit: Retrofit) = retrofit.create(PunkService::class.java)

}
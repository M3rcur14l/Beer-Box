package com.m3rc.beerbox.di

import android.app.Application
import com.m3rc.beerbox.di.module.ActivityBindingModule
import com.m3rc.beerbox.di.module.NetworkModule
import com.m3rc.beerbox.di.viewmodel.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ActivityBindingModule::class,
        ViewModelModule::class,
        NetworkModule::class]
)
interface AppComponent : AndroidInjector<Application> {

    override fun inject(application: Application)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}
package com.m3rc.beerbox.di

import com.m3rc.beerbox.BeerApplication
import com.m3rc.beerbox.di.module.ActivityBindingModule
import com.m3rc.beerbox.di.module.NetworkModule
import com.m3rc.beerbox.di.viewmodel.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ActivityBindingModule::class,
        ViewModelModule::class,
        NetworkModule::class]
)
interface AppComponent : AndroidInjector<BeerApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: BeerApplication): Builder

        fun build(): AppComponent
    }

    override fun inject(application: BeerApplication)

}

package com.m3rc.beerbox

import android.app.Application
import androidx.fragment.app.Fragment
import com.m3rc.beerbox.di.AppComponent
import com.m3rc.beerbox.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class Application : Application() {

    private val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .application(this)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }

}
